# Bibliothèque Scolaire — Architecture Microservices

Système de gestion de bibliothèque scolaire basé sur une architecture **microservices Spring Boot**.
Chaque service est indépendant, possède sa propre base de données, et communique via REST (OpenFeign) ou messagerie asynchrone (RabbitMQ).

---

## Table des matières

- [Architecture](#architecture)
- [Services](#services)
- [Stack technique](#stack-technique)
- [Prérequis](#prérequis)
- [Démarrage](#démarrage)
- [Sécurité](#sécurité)
- [Communication entre services](#communication-entre-services)
- [Frontend](#frontend)
- [Agent IA](#agent-ia)
- [Structure d'un service](#structure-dun-service)

---

## Architecture

```
                        ┌─────────────────┐
                        │   gateway-svc   │  ← Point d'entrée unique (port 8080)
                        │  Spring Gateway │     Validation JWT, routing, CORS
                        └────────┬────────┘
                                 │
          ┌──────────────────────┼──────────────────────┐
          │                      │                      │
   ┌──────▼──────┐       ┌───────▼──────┐      ┌───────▼──────┐
   │  livres-svc │       │ membres-svc  │      │ emprunts-svc │
   │  port 8082  │       │  port 8083   │      │  port 8082   │
   │  H2 / JPA   │       │  H2 / JPA    │      │  H2 / JPA    │
   └─────────────┘       └──────────────┘      └──────┬───────┘
                                                       │ RabbitMQ
                                               ┌───────▼───────┐
                                               │notification-svc│
                                               │   port 8084    │
                                               │  Email/Telegram│
                                               └───────────────┘

   ┌─────────────┐   ┌──────────────┐   ┌─────────────────┐
   │  auth-svc   │   │ rapports-svc │   │   chatbot-svc   │
   │  port 8085  │   │  port 8086   │   │   port 8087     │
   │     JWT     │   │ Python/FastAPI│  │  Spring AI MCP  │
   └─────────────┘   └──────────────┘   └─────────────────┘

   ┌─────────────────┐   ┌──────────────────┐
   │  discovery-svc  │   │   config-svc     │
   │  port 8761      │   │   port 8888      │
   │  Eureka Server  │   │  Spring Config   │
   └─────────────────┘   └──────────────────┘
```

---

## Services

| Service | Port | Description | Base de données |
|---|---|---|---|
| `gateway-svc` | 8080 | Point d'entrée unique, validation JWT, routing | — |
| `discovery-svc` | 8761 | Registre Eureka — tous les services s'y enregistrent | — |
| `config-svc` | 8888 | Configuration centralisée Spring Cloud Config | — |
| `auth-svc` | 8085 | Authentification JWT, login, rôles | `bd_authentification` |
| `livres-svc` | 8082 | Catalogue livres et exemplaires | `bd_livres` (H2) |
| `membres-svc` | 8083 | Élèves et professeurs | `bd_membres` (H2) |
| `emprunts-svc` | 8082 | Prêts, retours, réservations | `bd_emprunts` (H2) |
| `notification-svc` | 8084 | Email (JavaMail) + Telegram Bot | Stateless |
| `rapports-svc` | 8086 | Statistiques agrégées — **Python/FastAPI** | — |
| `chatbot-svc` | 8087 | Agent IA via Spring AI + MCP + OpenAI/Ollama | — |

### Détail des services

#### `livres-svc`
Gère le catalogue et les exemplaires physiques.
Entités : `Livre`, `Exemplaire`
Statuts d'un exemplaire : `DISPONIBLE`, `EMPRUNTE`, `RESERVE`, `PERDU`, `ABIME`

#### `membres-svc`
Gère les élèves et professeurs avec leurs quotas d'emprunts.
Entités : `Membre`, `Classe`
Rôles : `ELEVE`, `PROFESSEUR`

#### `emprunts-svc`
Gère les prêts, retours, prolongations et pénalités.
Entités : `Emprunt`, `Reservation`
Appelle `livres-svc` et `membres-svc` via **OpenFeign**.
Publie un événement sur **RabbitMQ** à chaque création d'emprunt.

#### `notification-svc`
Écoute la queue RabbitMQ `emprunt.cree` et envoie :
- Un **email** de confirmation via JavaMail (SMTP Gmail)
- Un message **Telegram** via le Bot API

#### `rapports-svc` *(Python)*
Seul service non-Java du projet — démontre l'**indépendance technologique** des microservices.
S'enregistre sur Eureka via `py-eureka-client`, routé automatiquement par la gateway.

#### `chatbot-svc`
Agent IA conversationnel qui interroge les autres services via **MCP (Model Context Protocol)**.
Compatible OpenAI (GPT-4o-mini) et Ollama (Llama3).

---

## Stack technique

| Composant | Technologie |
|---|---|
| Framework principal | Spring Boot 3.x, Java 21 |
| Gateway | Spring Cloud Gateway |
| Découverte de services | Netflix Eureka |
| Configuration centralisée | Spring Cloud Config |
| Sécurité | Spring Security + JWT (JJWT) |
| Communication synchrone | OpenFeign + Spring Cloud LoadBalancer |
| Communication asynchrone | RabbitMQ + Spring AMQP |
| Base de données | H2 (dev) / PostgreSQL (prod) |
| ORM | Spring Data JPA / Hibernate |
| Agent IA | Spring AI + MCP Streamable |
| LLM | OpenAI GPT-4o-mini / Ollama |
| Notifications | JavaMail + Telegram Bot API |
| Rapports | Python 3.11, FastAPI, httpx |
| Frontend | Angular 20, PrimeNG, TailwindCSS |
| Documentation API | SpringDoc OpenAPI (Swagger UI) |
| Résilience | Resilience4j (Circuit Breaker, TimeLimiter) |

---

## Prérequis

- Java 21+
- Maven 3.9+
- Node.js 20+ / npm
- Python 3.11+ (pour `rapports-svc`)
- RabbitMQ (local ou Docker)
- Ollama (optionnel, pour le mode LLM local)

### Démarrer RabbitMQ avec Docker

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:3-management
```

Interface RabbitMQ : [http://localhost:15672](http://localhost:15672) (guest/guest)

---

## Démarrage

### Ordre de démarrage obligatoire

```
1. config-svc       (8888)  ← en premier
2. discovery-svc    (8761)  ← en deuxième
3. auth-svc, livres-svc, membres-svc, emprunts-svc, notification-svc, chatbot-svc
4. rapports-svc     (8086)  ← Python
5. gateway-svc      (8080)  ← en dernier
```

### Lancer les services Java

```bash
# Compiler le projet
mvn clean install -DskipTests

# Démarrer chaque service
cd config-svc    && mvn spring-boot:run
cd discovery-svc && mvn spring-boot:run
cd livres-svc    && mvn spring-boot:run
# ... etc
```

### Lancer rapports-svc (Python)

```bash
cd rapports-svc
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python main.py
```

### Lancer le frontend Angular

```bash
cd front
npm install
ng serve
```

Frontend disponible sur [http://localhost:4200](http://localhost:4200)

---

## Sécurité

### Fonctionnement

- La `gateway-svc` est le **seul gardien du JWT** — les services internes ne revalident pas le token
- La gateway injecte les headers `X-User-Id` et `X-User-Role` dans chaque requête
- Les services font confiance à ces headers injectés

### Matrice des permissions

| Action | ELEVE | PROFESSEUR | BIBLIOTHECAIRE | ADMIN |
|---|:---:|:---:|:---:|:---:|
| Consulter le catalogue | ✅ | ✅ | ✅ | ✅ |
| Emprunter / Réserver | ✅ | ✅ | ✅ | ✅ |
| Ajouter / modifier un livre | ❌ | ❌ | ✅ | ✅ |
| Gérer les membres | ❌ | ❌ | ✅ | ✅ |
| Consulter les rapports | ❌ | ❌ | ✅ | ✅ |
| Gérer les utilisateurs | ❌ | ❌ | ❌ | ✅ |

---

## Communication entre services

### Synchrone — OpenFeign

```java
@FeignClient(name = "livres-svc", fallback = LivreClientFallback.class)
public interface LivreClient {
    @GetMapping("/livres/{id}")
    Livre getLivre(@PathVariable Long id);
}
```

Spring Cloud LoadBalancer assure le **load balancing** automatique entre les instances via Eureka.
Resilience4j gère le **circuit breaker** — si `livres-svc` est indisponible, le fallback prend le relais.

### Asynchrone — RabbitMQ

```
emprunts-svc  ──(emprunt.cree)──▶  RabbitMQ  ──▶  notification-svc
```

Quand un emprunt est créé, `emprunts-svc` publie un événement JSON dans la queue `emprunt.cree`.
`notification-svc` écoute cette queue et envoie l'email + le message Telegram sans bloquer `emprunts-svc`.

---

## Frontend

Application **Angular 20** standalone avec :
- **PrimeNG** — composants UI (tables, boutons, formulaires)
- **TailwindCSS** — styles utilitaires
- **marked** — rendu Markdown pour le chatbot

### Proxy de développement

Le fichier `proxy.conf.json` redirige les appels `/api/*` vers la gateway `localhost:8080` pour éviter les problèmes CORS en développement.

```
/api/livres   →  http://localhost:8080/livres-svc/livres
/api/membres  →  http://localhost:8080/membres-svc/membres
/api/emprunts →  http://localhost:8080/emprunts-svc/emprunts
/api/stats    →  http://localhost:8080/rapports-svc/stats
/api/chat     →  http://localhost:8080/chatbot-svc/chat
```

---

## Agent IA

Le `chatbot-svc` utilise **Spring AI** avec le protocole **MCP (Model Context Protocol)**.
Chaque service (`livres-svc`, `membres-svc`, `emprunts-svc`) expose des **outils MCP** que l'agent peut appeler pour répondre aux questions.

### Exemples de questions supportées

- *"Quels livres sont disponibles ?"*
- *"Quels emprunts sont en retard ?"*
- *"Combien de membres sont inscrits ?"*
- *"Quel membre a le plus d'emprunts ?"*

### Configuration LLM

```yaml
# OpenAI (nécessite OPENAI_API_KEY)
spring.ai.openai.chat.options.model: gpt-4o-mini

# Ollama (local, gratuit)
spring.ai.ollama.base-url: http://localhost:11434
spring.ai.ollama.chat.options.model: llama3.1:8b
```

---

## Structure d'un service

```
{nom}-svc/
├── src/main/java/sn/seydina/{nom}/
│   ├── controller/      ← REST endpoints
│   ├── service/         ← Logique métier
│   ├── repository/      ← Spring Data JPA
│   ├── model/           ← Entités @Entity
│   ├── dto/             ← Objets de transfert
│   ├── client/          ← @FeignClient vers autres services
│   ├── exception/       ← @RestControllerAdvice
│   └── config/          ← Beans de configuration
├── src/main/resources/
│   └── application.yaml
└── pom.xml
```

---

## Variables d'environnement

```env
JWT_SECRET=votre_cle_secrete_tres_longue
JWT_EXPIRATION=86400000
DB_HOST=localhost
DB_PORT=5432
DB_PASSWORD=votre_mot_de_passe
OPENAI_API_KEY=sk-...
TELEGRAM_TOKEN=votre_token_telegram
EUREKA_URL=http://localhost:8761/eureka
```

---

## Auteur

**Mouhamed Ndiaye** — Développeur Full Stack
Projet de démonstration — Architecture Microservices Spring Boot

# CLAUDE.md — Bibliothèque Scolaire Microservices

## Projet
Système de gestion de bibliothèque scolaire basé sur une architecture **microservices Spring Boot**.
Chaque service est indépendant, possède sa propre base de données, et communique via REST (OpenFeign) ou bus de messages (RabbitMQ / Kafka).

---

## Structure des modules Maven

```
biblio-ms-demo/                 ← Projet parent (ce repo)
├── auth-svc/                   ← JWT, login, rôles (port 8085)
├── gateway-svc/                ← Spring Cloud Gateway (port 8080)
├── discovery-svc/              ← Eureka Server (port 8761)
├── config-svc/                 ← Spring Cloud Config (port 8888)
├── livres-svc/                 ← Catalogue et exemplaires (port 8081)
├── emprunts-svc/               ← Prêts et retours (port 8082)
├── membres-svc/                ← Élèves et professeurs (port 8083)
├── notifications-svc/          ← Email et Telegram (port 8084, stateless)
├── rapports-svc/               ← Statistiques (port 8086)
├── chatbot-svc/                ← Agent IA MCP + LLM (port 8087)
├── security-lib/               ← JAR partagé (JwtUtil, JwtFilter, SecurityConfig)
└── docker-compose.yml
```

---

## Description des services

- **auth-svc** : génère/valide JWT, stocke users et rôles. Endpoints : `POST /auth/login`, `/auth/refresh`, `/auth/logout`. BDD : `bd_authentification`
- **gateway-svc** : seul point d'entrée exposé internet. Valide JWT, injecte `X-User-Id` et `X-User-Role`, route vers les services.
- **discovery-svc** : Eureka Server, registre de tous les services actifs.
- **config-svc** : centralise les `application.yml` de tous les services. Les services lisent leur config via `bootstrap.yml`.
- **livres-svc** : catalogue + exemplaires. Entités : `Livre`, `Exemplaire`. Statuts : `DISPONIBLE`, `EMPRUNTE`, `RESERVE`, `PERDU`, `ABIME`. BDD : `bd_livres`
- **emprunts-svc** : prêts, retours, prolongations, réservations, pénalités. Entités : `Emprunt`, `Reservation`. BDD : `bd_emprunts`
- **membres-svc** : élèves et professeurs, quotas d'emprunts. Entités : `Membre`, `Classe`. BDD : `bd_membres`
- **notifications-svc** : email (JavaMail) + Telegram Bot. Déclenché par événements RabbitMQ/Kafka. Stateless.
- **rapports-svc** : statistiques agrégées pour l'administration. BDD : `bd_rapports`
- **chatbot-svc** : agent IA via MCP Streamable + Claude/OpenAI. Interroge les autres services via Feign.
- **security-lib** : JAR partagé importé dans chaque service. Contient `JwtUtil`, `JwtFilter`, `SecurityConfig`.

---

## Sécurité

- `gateway-svc` est le seul gardien du JWT — les services internes ne revalident pas le token
- Les services font confiance aux headers `X-User-Id` / `X-User-Role` injectés par la gateway
- Rôles : `ELEVE`, `PROFESSEUR`, `BIBLIOTHECAIRE`, `ADMIN`

### Matrice des permissions

| Action                        | ELEVE | PROFESSEUR | BIBLIOTHECAIRE | ADMIN |
|-------------------------------|-------|------------|----------------|-------|
| Consulter le catalogue        | ✅    | ✅         | ✅             | ✅    |
| Rechercher un livre           | ✅    | ✅         | ✅             | ✅    |
| Emprunter / Réserver          | ✅    | ✅         | ✅             | ✅    |
| Ajouter / modifier un livre   | ❌    | ❌         | ✅             | ✅    |
| Gérer les membres             | ❌    | ❌         | ✅             | ✅    |
| Consulter les rapports        | ❌    | ❌         | ✅             | ✅    |
| Gérer les utilisateurs        | ❌    | ❌         | ❌             | ✅    |

---

## Structure interne d'un service (pattern à respecter)

```
{nom}-svc/
├── src/main/java/sn/seydina/bibliotheque/{nom}/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/        ← @Entity
│   ├── dto/
│   ├── client/       ← @FeignClient vers autres services
│   ├── exception/    ← @RestControllerAdvice
│   └── config/
├── src/main/resources/
│   ├── application.yml
│   └── bootstrap.yml  ← connexion à config-svc
└── pom.xml
```

---

## Communication entre services

- **Synchrone** : OpenFeign — `@FeignClient(name = "livres-svc")`
- **Asynchrone** : RabbitMQ (dev) / Kafka (prod)
  - `livre.retour.imminent` → notifications-svc
  - `livre.disponible` → notifications-svc
  - `emprunt.cree` → livres-svc (mise à jour stock)

---

## Stack technique

| Composant          | Technologie                         |
|--------------------|-------------------------------------|
| Framework          | Spring Boot 3.x, Java 21            |
| Gateway            | Spring Cloud Gateway                |
| Découverte         | Netflix Eureka                      |
| Configuration      | Spring Cloud Config                 |
| Sécurité           | Spring Security + JWT (JJWT)        |
| Communication REST | OpenFeign                           |
| Bus de messages    | RabbitMQ (dev) / Kafka (prod)       |
| Base de données    | PostgreSQL (database-per-service)   |
| ORM                | Spring Data JPA / Hibernate         |
| Migrations BDD     | Flyway                              |
| Conteneurisation   | Docker + Docker Compose             |
| Agent IA           | MCP Streamable + Claude / OpenAI    |
| Notifications      | JavaMail + Telegram Bot API         |

---

## Ordre de démarrage

```
1. config-svc      (8888)
2. discovery-svc   (8761)
3. auth-svc, livres-svc, emprunts-svc, membres-svc, notifications-svc, rapports-svc, chatbot-svc
4. gateway-svc     (8080) — en dernier
```

---

## Variables d'environnement

```env
JWT_SECRET=votre_cle_secrete_tres_longue_et_complexe
JWT_EXPIRATION=86400000
DB_HOST=localhost
DB_PORT=5432
DB_PASSWORD=votre_mot_de_passe
EUREKA_URL=http://discovery-svc:8761/eureka
CONFIG_URL=http://config-svc:8888
TELEGRAM_BOT_TOKEN=votre_token_telegram
```

---

## Conventions Git
- Messages de commit en français, simples et courts
- Pas de co-auteur Claude

## Contexte du projet
- Ce projet est une **démo** — rester simple dans toutes les décisions techniques
- Pas de sur-ingénierie : pas de DTO, pas de validation complexe, pas d'abstraction inutile

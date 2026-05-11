import httpx

GATEWAY = "http://localhost:8080"

async def fetch(url: str):
    async with httpx.AsyncClient(timeout=5) as client:
        r = await client.get(url)
        r.raise_for_status()
        return r.json()

async def stats_livres():
    livres = await fetch(f"{GATEWAY}/livres-svc/livres")
    total = len(livres)
    disponibles = sum(1 for l in livres if l.get("quantiteDisponible", 0) > 0)
    return {
        "total": total,
        "disponibles": disponibles,
        "empruntes": total - disponibles
    }

async def stats_membres():
    membres = await fetch(f"{GATEWAY}/membres-svc/membres")
    return {
        "total": len(membres),
        "eleves": sum(1 for m in membres if m.get("role") == "ELEVE"),
        "professeurs": sum(1 for m in membres if m.get("role") == "PROFESSEUR")
    }

async def stats_emprunts():
    emprunts = await fetch(f"{GATEWAY}/emprunts-svc/emprunts")
    return {
        "en_cours": sum(1 for e in emprunts if e.get("statut") == "EN_COURS"),
        "en_retard": sum(1 for e in emprunts if e.get("statut") == "EN_RETARD"),
        "retournes": sum(1 for e in emprunts if e.get("statut") == "RETOURNE")
    }

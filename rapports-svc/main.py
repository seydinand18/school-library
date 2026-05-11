import asyncio
import py_eureka_client.eureka_client as eureka_client
from fastapi import FastAPI, HTTPException
from stats import stats_livres, stats_membres, stats_emprunts

app = FastAPI(title="rapports-svc", version="1.0.0")
PORT = 8086

@app.on_event("startup")
async def startup():
    await eureka_client.init_async(
        eureka_server="http://localhost:8761/eureka",
        app_name="rapports-svc",
        instance_port=PORT,
        instance_host="localhost"
    )

@app.get("/stats")
async def dashboard():
    try:
        livres, membres, emprunts = await asyncio.gather(
            stats_livres(), stats_membres(), stats_emprunts()
        )
        return {"livres": livres, "membres": membres, "emprunts": emprunts}
    except Exception as e:
        raise HTTPException(status_code=502, detail=str(e))

@app.get("/stats/livres")
async def route_livres():
    try:
        return await stats_livres()
    except Exception as e:
        raise HTTPException(status_code=502, detail=str(e))

@app.get("/stats/membres")
async def route_membres():
    try:
        return await stats_membres()
    except Exception as e:
        raise HTTPException(status_code=502, detail=str(e))

@app.get("/stats/emprunts")
async def route_emprunts():
    try:
        return await stats_emprunts()
    except Exception as e:
        raise HTTPException(status_code=502, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=PORT, reload=False)

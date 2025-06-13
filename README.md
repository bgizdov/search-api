# search-api

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/search-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## API Endpoints

The application provides the following search endpoints for retrieving data from Elasticsearch:

### Football Matches
- **GET** `/api/matches` - Search football match data
- Query parameters:
  - `q` (optional): Search query string
  - `size` (optional, default: 10): Number of results to return

### Predictions
- **GET** `/api/predictions` - Search match predictions
- Query parameters:
  - `q` (optional): Search query string
  - `size` (optional, default: 10): Number of results to return

### Quiz Games
- **GET** `/api/quiz-games` - Search quiz games
- Query parameters:
  - `q` (optional): Search query string
  - `size` (optional, default: 10): Number of results to return

### Player of the Match Games
- **GET** `/api/player-games` - Search player of the match games
- Query parameters:
  - `q` (optional): Search query string
  - `size` (optional, default: 10): Number of results to return

### Example Usage
```bash
# Get all matches (up to 10)
curl "http://localhost:8082/api/matches"

# Search for specific matches
curl "http://localhost:8082/api/matches?q=Barcelona&size=5"

# Get all predictions
curl "http://localhost:8082/api/predictions"
```

## Elasticsearch Setup with Dev Services

This application uses **Quarkus Dev Services** to automatically start Elasticsearch during development and testing. No manual setup required!

### Quick Start

```bash
# Just start the application - Elasticsearch starts automatically
./mvnw quarkus:dev

# Application available at: http://localhost:8082
# Test endpoints (work immediately, return data once Elasticsearch is ready)
curl "http://localhost:8082/api/matches"
curl "http://localhost:8082/health/elasticsearch"
```

### Key Features

- ✅ **Zero Configuration** - No manual Elasticsearch setup needed
- ✅ **Automatic Container Management** - Docker containers start/stop automatically
- ✅ **Container Reuse** - Faster restarts with persistent containers
- ✅ **Sample Data** - Automatically loads test data when ready
- ✅ **Isolated Testing** - Separate containers for tests

### Sample Data

The application automatically creates sample data:
- `football_matches` - Sample football match data
- `predictions` - Sample match predictions
- `quiz_games` - Sample quiz games
- `player_games` - Sample player of the match games

### Speed Up Development

Enable container reuse for faster startup times:

1. Create `~/.testcontainers.properties`:
   ```properties
   testcontainers.reuse.enable=true
   ```

2. Containers will persist between application restarts

### Detailed Documentation

📚 **See [ELASTICSEARCH_DEV_SERVICES.md](ELASTICSEARCH_DEV_SERVICES.md) for complete Dev Services documentation including:**
- Configuration options
- Container reuse setup
- Troubleshooting guide
- Production configuration

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Elasticsearch REST client ([guide](https://quarkus.io/guides/elasticsearch)): Connect to an Elasticsearch cluster using the REST low level client

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

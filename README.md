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

### Unified Search API

The application also provides a unified search endpoint that can search across all entity types with configurable search modes:

**GET** `/api/search` - Unified search across all types
- Query parameters:
  - `q` (optional): Search query string
  - `type` (optional): Specific entity type to search
  - `id` (optional): Find specific entity by ID (requires type)
  - `size` (optional, default: 10): Number of results to return
  - `mode` (optional, default: case_insensitive): Search mode

**GET** `/api/search/modes` - Get available search modes

#### Search Modes

- **`case_insensitive`** (default): Case insensitive partial matching
  - Example: `"game"` matches `"Game 21"`, `"Player Game"`, `"GAME"`
- **`case_sensitive`**: Case sensitive partial matching
  - Example: `"Game"` matches `"Game 21"`, `"Player Game"` but not `"game"` or `"GAME"`
- **`full_match`**: Full string match (case insensitive)
  - Example: `"Player of the Match Game 21"` only matches exact title, not `"Game 21"` or `"Game 22"`

### Example Usage
```bash
# Get all matches (up to 10)
curl "http://localhost:8082/api/matches"

# Search for specific matches
curl "http://localhost:8082/api/matches?q=Barcelona&size=5"

# Get all predictions
curl "http://localhost:8082/api/predictions"

# Unified search across all types
curl "http://localhost:8082/api/search?q=Barcelona&size=10"

# Search specific type with case sensitive mode
curl "http://localhost:8082/api/search?type=matches&q=Barcelona&mode=case_sensitive"

# Full match search for exact titles
curl "http://localhost:8082/api/search?q=Player%20of%20the%20Match%20Game%2021&mode=full_match"

# Find specific entity by ID
curl "http://localhost:8082/api/search?type=matches&id=1"

# Get available search modes
curl "http://localhost:8082/api/search/modes"
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

The application automatically creates sample data based on configuration:

### Sample Data Modes

Configure sample data loading in `application.properties`:

```properties
# Sample Data Configuration
# Options: NONE, BASIC, PERFORMANCE_SMALL, PERFORMANCE_LARGE
# Performance tests are disabled by default (use BASIC mode)
app.sample-data.mode=BASIC
app.sample-data.records-per-type=2500
```

**Available modes:**

- **`NONE`** - No sample data loaded
- **`BASIC`** - Basic sample data (few records for development) **[DEFAULT]**
  - `football_matches` - 3 sample matches
  - `predictions` - 3 sample predictions
  - `quiz_games` - 2 sample quiz games
  - `player_games` - 2 sample player games
  - ⚡ **Fast startup** - loads in seconds

- **`PERFORMANCE_SMALL`** - Performance test data (configurable size) **[OPTIONAL]**
  - Uses `app.sample-data.records-per-type` setting (default: 2,500 per type = 10k total)
  - Good for testing search performance with moderate data
  - ⏱️ **Moderate startup** - loads in ~30 seconds

- **`PERFORMANCE_LARGE`** - Large performance test data **[OPTIONAL]**
  - 250,000 records per type (1 million total records)
  - Suitable for stress testing and performance benchmarking
  - 🐌 **Slow startup** - loads in several minutes

### Performance Data Features

When using `PERFORMANCE_SMALL` or `PERFORMANCE_LARGE` modes:

- **Realistic data variety**: Multiple teams, venues, competitions, users
- **Bulk loading**: Efficient batch insertion with 1000-record batches
- **Progress logging**: Shows insertion progress for large datasets
- **Automatic indexing**: Refreshes Elasticsearch indices after loading
- **Reference integrity**: Predictions and player games reference match IDs

### Quick Start with Sample Data

**Basic development (default - fast startup):**
```bash
./mvnw quarkus:dev
# Loads basic sample data (few records) - PERFORMANCE TESTS DISABLED BY DEFAULT
# ✅ Fast startup in seconds
```

**Enable performance testing (10k records):**
```bash
./mvnw quarkus:dev -Dapp.sample-data.mode=PERFORMANCE_SMALL
# Loads 2,500 records per type (10k total)
# ⏱️ Startup in ~30 seconds
```

**Enable stress testing (1M records):**
```bash
./mvnw quarkus:dev -Dquarkus.profile=stress-test
# Loads 250k records per type (1M total)
# 🐌 Startup in several minutes
```

**Custom performance configuration:**
```bash
./mvnw quarkus:dev -Dapp.sample-data.mode=PERFORMANCE_SMALL -Dapp.sample-data.records-per-type=5000
# Loads 5k records per type (20k total)
```

**Disable all sample data:**
```bash
./mvnw quarkus:dev -Dapp.sample-data.mode=NONE
# No sample data loaded - fastest startup
```

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

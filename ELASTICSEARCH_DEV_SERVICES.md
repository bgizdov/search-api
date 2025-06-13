# Elasticsearch Dev Services Guide

This project uses **Quarkus Dev Services for Elasticsearch** to automatically manage Elasticsearch containers during development and testing.

## 🚀 Quick Start

```bash
# Just start the application - Elasticsearch will start automatically
./mvnw quarkus:dev

# Application available at: http://localhost:8082
# Elasticsearch will be available once the container starts
```

## 🔧 Configuration

### Development Mode

Dev Services are configured in `src/main/resources/application.properties`:

```properties
# Elasticsearch Dev Services Configuration
quarkus.elasticsearch.devservices.enabled=true
quarkus.elasticsearch.devservices.image-name=docker.io/elastic/elasticsearch:8.15.0
quarkus.elasticsearch.devservices.java-opts=-Xms256m -Xmx512m
quarkus.elasticsearch.devservices.shared=true
quarkus.elasticsearch.devservices.service-name=elasticsearch
quarkus.elasticsearch.devservices.reuse=true
```

### Test Mode

Tests use isolated containers configured in `src/test/resources/application.properties`:

```properties
# Elasticsearch Dev Services for tests
quarkus.elasticsearch.devservices.enabled=true
quarkus.elasticsearch.devservices.shared=false
quarkus.elasticsearch.devservices.reuse=false
```

## ⚡ Speed Up Development with Container Reuse

To keep Elasticsearch containers running between application restarts:

1. **Enable TestContainers reuse** by creating/editing `~/.testcontainers.properties`:
   ```properties
   testcontainers.reuse.enable=true
   ```

2. **Benefits:**
   - Faster startup times (container doesn't restart)
   - Persistent data between runs
   - Shared containers across multiple Quarkus apps

3. **Important Notes:**
   - Data persists between runs (good for development)
   - May need manual cleanup of old containers
   - Only works when configuration doesn't change

## 🐳 Container Management

### Automatic Behavior

- **Dev Mode**: Containers start automatically when you run `./mvnw quarkus:dev`
- **Test Mode**: Fresh containers for each test run (unless reuse is enabled)
- **Shared Containers**: Multiple apps can share the same Elasticsearch instance
- **Auto-Configuration**: Quarkus automatically configures connection settings

### Manual Container Management

```bash
# View running containers
docker ps

# Stop all Elasticsearch Dev Services containers
docker stop $(docker ps -q --filter "label=quarkus-dev-service-elasticsearch")

# Remove old containers (if reuse is enabled)
docker rm $(docker ps -aq --filter "label=quarkus-dev-service-elasticsearch")
```

## 📊 API Endpoints

Once Elasticsearch is running, these endpoints will return data:

```bash
# Search endpoints
curl "http://localhost:8082/api/matches"
curl "http://localhost:8082/api/predictions"
curl "http://localhost:8082/api/quiz-games"
curl "http://localhost:8082/api/player-games"

# Health check
curl "http://localhost:8082/health/elasticsearch"
```

## 🔍 Troubleshooting

### Dev Services Not Starting

1. **Check Docker**: Ensure Docker is running
   ```bash
   docker --version
   docker ps
   ```

2. **Check Configuration**: Ensure `quarkus.elasticsearch.hosts` is not set
   ```properties
   # This disables Dev Services:
   # quarkus.elasticsearch.hosts=localhost:9200
   ```

3. **Enable Debug Logging**:
   ```properties
   quarkus.log.category."io.quarkus.devservices".level=DEBUG
   ```

### Slow Startup

- **First Run**: Container image download takes time
- **Elasticsearch Startup**: ES needs 30-60 seconds to fully initialize
- **Solution**: Enable container reuse (see above)

### Connection Refused Errors

- **Normal During Startup**: Application starts before Elasticsearch is ready
- **Check Health Endpoint**: `curl http://localhost:8082/health/elasticsearch`
- **Wait**: Give Elasticsearch 1-2 minutes to fully start

### Port Conflicts

Dev Services automatically pick available ports. If you need a specific port:

```properties
quarkus.elasticsearch.devservices.port=9200
```

## 🧪 Testing

Tests automatically use isolated Elasticsearch containers:

```bash
# Run tests (starts fresh Elasticsearch containers)
./mvnw test

# Tests are configured to handle both connected and disconnected states
```

## 📝 Sample Data

The application automatically loads sample data when Elasticsearch becomes available:

- **Football Matches**: Sample match data
- **Predictions**: Sample prediction data  
- **Quiz Games**: Sample quiz data
- **Player Games**: Sample player of the match games

Data initialization happens in the background and doesn't block application startup.

## 🔄 Production Configuration

For production, disable Dev Services and configure real Elasticsearch:

```properties
# Disable Dev Services
quarkus.elasticsearch.devservices.enabled=false

# Configure production Elasticsearch
quarkus.elasticsearch.hosts=your-elasticsearch-host:9200
quarkus.elasticsearch.username=your-username
quarkus.elasticsearch.password=your-password
```

## 📚 Additional Resources

- [Quarkus Dev Services Documentation](https://quarkus.io/guides/dev-services)
- [Elasticsearch REST Client Guide](https://quarkus.io/guides/elasticsearch)
- [TestContainers Documentation](https://www.testcontainers.org/)

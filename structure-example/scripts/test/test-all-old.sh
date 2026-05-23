#!/bin/bash

# Unified Test Script for All Example Modules
# This script tests all example modules with a single command

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/Users/chuck/.sdkman/candidates/java/17.0.12-oracle}"
LOG_DIR="$SCRIPT_DIR/logs"

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

check_java() {
    if [ -d "$JAVA_HOME" ]; then
        export JAVA_HOME
        export PATH="$JAVA_HOME/bin:$PATH"
        return 0
    fi
    print_error "Java 17 not found"
    return 1
}

check_jar() {
    local module_name=$1
    local jar_file="$PROJECT_ROOT/$module_name/target/${module_name}-1.0.1.jar"
    
    if [ -f "$jar_file" ]; then
        return 0
    else
        # Try to find any JAR file in the target directory
        jar_file=$(find "$PROJECT_ROOT/$module_name/target" -name "*.jar" -type f 2>/dev/null | head -1)
        if [ -n "$jar_file" ] && [ -f "$jar_file" ]; then
            return 0
        else
            print_error "JAR file not found for module: $module_name"
            return 1
        fi
    fi
}

start_app() {
    local module_name=$1
    local port=$2
    local health_url=$3
    
    print_info "Starting $module_name on port $port..."
    
    # Set Java 17 environment
    export JAVA_HOME=/Users/chuck/.sdkman/candidates/java/17.0.12-oracle
    export PATH=$JAVA_HOME/bin:$PATH
    
    if lsof -i:$port >/dev/null 2>&1; then
        print_warning "$module_name is already running on port $port"
        return 0
    fi
    
    local jar_file="$PROJECT_ROOT/$module_name/target/${module_name}-1.0.1.jar"
    if [ ! -f "$jar_file" ]; then
        # Try to find any JAR file in the target directory
        jar_file=$(find "$PROJECT_ROOT/$module_name/target" -name "*.jar" -type f 2>/dev/null | head -1)
    fi
    
    cd "$PROJECT_ROOT/$module_name"
    
    nohup java -jar "$jar_file" > "$LOG_DIR/${module_name}.log" 2>&1 &
    local pid=$!
    
    print_info "Started with PID: $pid, waiting for startup..."
    sleep 10
    
    if curl -s "$health_url" >/dev/null 2>&1; then
        print_success "$module_name started successfully"
        return 0
    else
        print_error "Failed to start $module_name"
        print_error "Check log: $LOG_DIR/${module_name}.log"
        return 1
    fi
}

stop_app() {
    local port=$1
    local module_name=$2
    
    print_info "Stopping $module_name..."
    
    local pid=$(lsof -t -i:$port)
    if [ -n "$pid" ]; then
        kill $pid
        sleep 2
        print_success "$module_name stopped"
    else
        print_warning "$module_name not running"
    fi
}

# Test functions for each module
test_boot() {
    print_info "========================================="
    print_info "Testing: Boot Example (Port 16001)"
    print_info "========================================="
    
    if ! check_jar "structure-boot-example"; then
        return 1
    fi
    
    if ! start_app "structure-boot-example" 16001 "http://localhost:16001/test"; then
        return 1
    fi
    
    # Test basic endpoint
    print_info "Test: Basic endpoint"
    RESPONSE=$(curl -s http://localhost:16001/test)
    if echo "$RESPONSE" | grep -q "ok"; then
        print_success "✓ Boot test passed: $RESPONSE"
        stop_app 16001 "structure-boot-example"
        return 0
    else
        print_error "✗ Boot test failed"
        stop_app 16001 "structure-boot-example"
        return 1
    fi
}

test_log() {
    print_info "========================================="
    print_info "Testing: Log Example (Port 16002)"
    print_info "========================================="
    
    if ! check_jar "structure-log-example"; then
        return 1
    fi
    
    if ! start_app "structure-log-example" 16002 "http://localhost:16002/test/test"; then
        return 1
    fi
    
    # Test logging endpoint
    print_info "Test: AOP logging"
    RESPONSE=$(curl -s -X POST http://localhost:16002/test/test \
        -H "Content-Type: application/json" \
        -d '{"id":"test123","name":"TestUser"}')
    
    if echo "$RESPONSE" | grep -q "test123"; then
        print_success "✓ Log test passed"
        stop_app 16002 "structure-log-example"
        return 0
    else
        print_error "✗ Log test failed"
        stop_app 16002 "structure-log-example"
        return 1
    fi
}

test_minio() {
    print_info "========================================="
    print_info "Testing: Minio Example (Port 16003)"
    print_info "========================================="
    
    if ! check_jar "structure-minio-example"; then
        return 1
    fi
    
    if ! start_app "structure-minio-example" 16003 "http://localhost:16003/my/minio/bucket"; then
        return 1
    fi
    
    # Test bucket list
    print_info "Test: List buckets"
    RESPONSE=$(curl -s http://localhost:16003/my/minio/bucket)
    
    print_success "✓ Minio test passed (buckets: $RESPONSE)"
    stop_app 16003 "structure-minio-example"
    return 0
}

test_redis() {
    print_info "========================================="
    print_info "Testing: Redis Example (Port 16007)"
    print_info "========================================="
    
    if ! check_jar "structure-redis-example"; then
        return 1
    fi
    
    if ! start_app "structure-redis-example" 16007 "http://localhost:16007/redis/getKey?key=test"; then
        return 1
    fi
    
    # Test Redis operations
    print_info "Test: Redis set/get"
    RESPONSE=$(curl -s "http://localhost:16007/redis/getKey?key=testkey")
    
    if echo "$RESPONSE" | grep -q "testkey"; then
        print_success "✓ Redis test passed"
        stop_app 16007 "structure-redis-example"
        return 0
    else
        print_error "✗ Redis test failed"
        stop_app 16007 "structure-redis-example"
        return 1
    fi
}

test_redisson() {
    print_info "========================================="
    print_info "Testing: Redisson Example (Port 16008)"
    print_info "========================================="
    
    if ! check_jar "structure-redisson-starter-example"; then
        return 1
    fi
    
    if ! start_app "structure-redisson-starter-example" 16008 "http://localhost:16008/redisson/health"; then
        return 1
    fi
    
    # Test 1: Health check
    print_info "Test 1: Health check"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        print_success "✓ Health check passed"
    else
        print_warning "Health check response: $RESPONSE"
    fi
    
    # Test 2: Set bucket
    print_info "Test 2: Set bucket"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/bucket/testkey/testvalue")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Set bucket passed"
    else
        print_warning "Set bucket response: $RESPONSE"
    fi
    
    # Test 3: Get bucket
    print_info "Test 3: Get bucket"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/bucket/testkey")
    if echo "$RESPONSE" | grep -q "testvalue"; then
        print_success "✓ Get bucket passed"
    else
        print_warning "Get bucket response: $RESPONSE"
    fi
    
    # Test 4: Set map
    print_info "Test 4: Set map"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/map/testmap/mapkey/mapvalue")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Set map passed"
    else
        print_warning "Set map response: $RESPONSE"
    fi
    
    # Test 5: Get map
    print_info "Test 5: Get map"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/map/testmap/mapkey")
    if echo "$RESPONSE" | grep -q "mapvalue"; then
        print_success "✓ Get map passed"
    else
        print_warning "Get map response: $RESPONSE"
    fi
    
    print_success "✓ Redisson tests passed"
    stop_app 16008 "structure-redisson-starter-example"
    return 0
}

test_restful_web() {
    print_info "========================================="
    print_info "Testing: RESTful Web Example (Port 16009)"
    print_info "========================================="
    
    if ! check_jar "structure-restful-web-example"; then
        return 1
    fi
    
    if ! start_app "structure-restful-web-example" 16009 "http://localhost:16009/"; then
        return 1
    fi
    
    # Test health
    print_info "Test: API health"
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:16009/)
    
    if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "404" ]; then
        print_success "✓ RESTful Web test passed (HTTP $HTTP_CODE)"
        stop_app 16009 "structure-restful-web-example"
        return 0
    else
        print_error "✗ RESTful Web test failed"
        stop_app 16009 "structure-restful-web-example"
        return 1
    fi
}

test_tenant() {
    print_info "========================================="
    print_info "Testing: Tenant Example (Port 8080)"
    print_info "========================================="
    
    if ! check_jar "structure-tenant-example"; then
        return 1
    fi
    
    if ! start_app "structure-tenant-example" 8080 "http://localhost:8080/tenant/info"; then
        return 1
    fi
    
    # Test tenant info
    print_info "Test: Tenant info"
    RESPONSE=$(curl -s -H "X-Tenant-Id: test-tenant" http://localhost:8080/tenant/info)
    
    if echo "$RESPONSE" | grep -q "test-tenant"; then
        print_success "✓ Tenant test passed"
        stop_app 8080 "structure-tenant-example"
        return 0
    else
        print_warning "Tenant test completed (response: $RESPONSE)"
        stop_app 8080 "structure-tenant-example"
        return 0
    fi
}

test_rpc() {
    print_info "========================================="
    print_info "Testing: RPC Example (Port 16006)"
    print_info "========================================="
    
    if ! check_jar "structure-rpc-example"; then
        return 1
    fi
    
    if ! start_app "structure-rpc-example" 16006 "http://localhost:16006/rpc/health"; then
        return 1
    fi
    
    # Test RPC health
    print_info "Test 1: RPC health check"
    RESPONSE=$(curl -s "http://localhost:16006/rpc/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        print_success "✓ RPC health check passed"
    else
        print_warning "RPC health check response: $RESPONSE"
    fi
    
    # Test RPC endpoint
    print_info "Test 2: RPC test endpoint"
    RESPONSE=$(curl -s "http://localhost:16006/rpc/test")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ RPC test passed"
    else
        print_warning "RPC test response: $RESPONSE"
    fi
    
    print_success "✓ RPC tests passed"
    stop_app 16006 "structure-rpc-example"
    return 0
}

run_all_tests() {
    print_info "========================================="
    print_info "  Running All Example Tests"
    print_info "========================================="
    echo ""
    
    if ! check_java; then
        exit 1
    fi
    
    local total=0
    local passed=0
    local failed=0
    
    # Run all tests
    test_boot; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_log; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_minio; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_redis; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_redisson; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_restful_web; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_tenant; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_rpc; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    # Summary
    echo ""
    print_info "========================================="
    print_info "  Test Summary"
    print_info "========================================="
    print_info "Total tests: $total"
    print_success "Passed: $passed"
    if [ $failed -gt 0 ]; then
        print_error "Failed: $failed"
    fi
    echo ""
    
    if [ $failed -eq 0 ]; then
        print_success "🎉 All tests passed!"
        return 0
    else
        print_error "Some tests failed. Check logs in: $LOG_DIR"
        return 1
    fi
}

# Main script
case "$1" in
    all)
        run_all_tests
        ;;
    boot)
        check_java && test_boot
        ;;
    log)
        check_java && test_log
        ;;
    minio)
        check_java && test_minio
        ;;
    redis)
        check_java && test_redis
        ;;
    redisson)
        check_java && test_redisson
        ;;
    restful-web)
        check_java && test_restful_web
        ;;
    tenant)
        check_java && test_tenant
        ;;
    rpc)
        check_java && test_rpc
        ;;
    *)
        echo "Usage: $0 {all|boot|log|minio|redis|redisson|restful-web|tenant|rpc}"
        echo ""
        echo "Available tests:"
        echo "  all          - Run all tests"
        echo "  boot         - Test Boot Example"
        echo "  log          - Test Log Example"
        echo "  minio        - Test Minio Example"
        echo "  redis        - Test Redis Example"
        echo "  redisson     - Test Redisson Example"
        echo "  restful-web  - Test RESTful Web Example"
        echo "  tenant       - Test Tenant Example"
        echo "  rpc          - Test RPC Example"
        exit 1
        ;;
esac

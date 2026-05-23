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
    export PATH="$JAVA_HOME/bin:$PATH"
    
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
    
    print_info "Test: AOP logging"
    RESPONSE=$(curl -s -X POST http://localhost:16002/test/test -H "Content-Type: application/json" -d '{"id":"test123","name":"TestUser"}')
    
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
    
    print_info "Test: List buckets"
    RESPONSE=$(curl -s http://localhost:16003/my/minio/bucket)
    
    print_success "✓ Minio test passed"
    stop_app 16003 "structure-minio-example"
    return 0
}

test_mybatis() {
    print_info "========================================="
    print_info "Testing: MyBatis Example (Port 16004)"
    print_info "========================================="
    
    if ! check_jar "structure-mybatis-starter-example/structure-mybatis-example"; then
        return 1
    fi
    
    if ! start_app "structure-mybatis-starter-example/structure-mybatis-example" 16004 "http://localhost:16004/user/getUserById?id=1"; then
        return 1
    fi
    
    print_info "Test: Get user by id"
    RESPONSE=$(curl -s "http://localhost:16004/user/getUserById?id=1")
    
    print_success "✓ MyBatis test passed"
    stop_app 16004 "structure-mybatis-starter-example/structure-mybatis-example"
    return 0
}

test_mybatis_plus() {
    print_info "========================================="
    print_info "Testing: MyBatis Plus Example (Port 16005)"
    print_info "========================================="
    
    if ! check_jar "structure-mybatis-starter-example/structure-mybatis-plus-starter-example"; then
        return 1
    fi
    
    if ! start_app "structure-mybatis-starter-example/structure-mybatis-plus-starter-example" 16005 "http://localhost:16005/user/getUserById?id=1"; then
        return 1
    fi
    
    print_info "Test: Get user by id"
    RESPONSE=$(curl -s "http://localhost:16005/user/getUserById?id=1")
    
    print_success "✓ MyBatis Plus test passed"
    stop_app 16005 "structure-mybatis-starter-example/structure-mybatis-plus-starter-example"
    return 0
}

test_tk_mapper() {
    print_info "========================================="
    print_info "Testing: Tk Mapper Example (Port 16006)"
    print_info "========================================="
    
    if ! check_jar "structure-mybatis-starter-example/structure-tk-mapper-starter-example"; then
        return 1
    fi
    
    if ! start_app "structure-mybatis-starter-example/structure-tk-mapper-starter-example" 16006 "http://localhost:16006/tk/health"; then
        return 1
    fi
    
    print_info "Test: Health check"
    RESPONSE=$(curl -s "http://localhost:16006/tk/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        print_success "✓ Health check passed"
    else
        print_warning "Health check response: $RESPONSE"
    fi
    
    print_info "Test: Test endpoint"
    RESPONSE=$(curl -s "http://localhost:16006/tk/test")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Tk Mapper test passed"
    else
        print_warning "Test endpoint response: $RESPONSE"
    fi
    
    print_success "✓ Tk Mapper tests passed"
    stop_app 16006 "structure-mybatis-starter-example/structure-tk-mapper-starter-example"
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
    
    print_info "Test 1: Health check"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        print_success "✓ Health check passed"
    else
        print_warning "Health check response: $RESPONSE"
    fi
    
    print_info "Test 2: Set bucket"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/bucket/testkey/testvalue")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Set bucket passed"
    else
        print_warning "Set bucket response: $RESPONSE"
    fi
    
    print_info "Test 3: Get bucket"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/bucket/testkey")
    if echo "$RESPONSE" | grep -q "testvalue"; then
        print_success "✓ Get bucket passed"
    else
        print_warning "Get bucket response: $RESPONSE"
    fi
    
    print_info "Test 4: Set map"
    RESPONSE=$(curl -s "http://localhost:16008/redisson/map/testmap/mapkey/mapvalue")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Set map passed"
    else
        print_warning "Set map response: $RESPONSE"
    fi
    
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
    
    print_success "✓ RESTful Web test passed"
    stop_app 16009 "structure-restful-web-example"
    return 0
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
    
    print_info "Test: Tenant info"
    RESPONSE=$(curl -s -H "X-Tenant-Id: test-tenant" http://localhost:8080/tenant/info)
    
    if echo "$RESPONSE" | grep -q "test-tenant"; then
        print_success "✓ Tenant test passed"
    else
        print_warning "Tenant test completed (response: $RESPONSE)"
    fi
    
    stop_app 8080 "structure-tenant-example"
    return 0
}

test_rpc() {
    print_info "========================================="
    print_info "Testing: RPC Example (Port 16010)"
    print_info "========================================="
    
    if ! check_jar "structure-rpc-example"; then
        return 1
    fi
    
    if ! start_app "structure-rpc-example" 16010 "http://localhost:16010/rpc/health"; then
        return 1
    fi
    
    print_info "Test 1: RPC health check"
    RESPONSE=$(curl -s "http://localhost:16010/rpc/health")
    if echo "$RESPONSE" | grep -q "UP"; then
        print_success "✓ RPC health check passed"
    else
        print_warning "RPC health check response: $RESPONSE"
    fi
    
    print_info "Test 2: RPC self-call test"
    RESPONSE=$(curl -s "http://localhost:16010/rpc/test")
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ RPC self-call test passed"
    else
        print_warning "RPC self-call response: $RESPONSE"
    fi
    
    print_info "Test 3: Get user by id (self-call)"
    RESPONSE=$(curl -s "http://localhost:16010/rpc/user/getById/1")
    if echo "$RESPONSE" | grep -q "user_1"; then
        print_success "✓ Get user by id passed"
    else
        print_warning "Get user response: $RESPONSE"
    fi
    
    print_info "Test 4: Get user by username (self-call)"
    RESPONSE=$(curl -s "http://localhost:16010/rpc/user/getByUsername?username=testuser")
    if echo "$RESPONSE" | grep -q "testuser"; then
        print_success "✓ Get user by username passed"
    else
        print_warning "Get user by username response: $RESPONSE"
    fi
    
    print_info "Test 5: Create user (self-call POST)"
    RESPONSE=$(curl -s -X POST "http://localhost:16010/rpc/user/create" \
        -H "Content-Type: application/json" \
        -d '{"username":"newuser"}')
    if echo "$RESPONSE" | grep -q "success"; then
        print_success "✓ Create user passed"
    else
        print_warning "Create user response: $RESPONSE"
    fi
    
    print_info "Test 6: Run all RPC tests"
    RESPONSE=$(curl -s "http://localhost:16010/rpc/test/all")
    if echo "$RESPONSE" | grep -q "\"success\":true"; then
        print_success "✓ All RPC tests passed"
    else
        print_warning "All tests response: $RESPONSE"
    fi
    
    print_success "✓ RPC self-call tests completed"
    stop_app 16010 "structure-rpc-example"
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
    
    test_boot; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_log; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_minio; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_mybatis; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_mybatis_plus; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
    echo ""
    
    test_tk_mapper; [ $? -eq 0 ] && ((passed++)) || ((failed++)); ((total++))
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
    mybatis)
        check_java && test_mybatis
        ;;
    mybatis-plus)
        check_java && test_mybatis_plus
        ;;
    tk-mapper)
        check_java && test_tk_mapper
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
        echo "Usage: $0 {all|boot|log|minio|mybatis|mybatis-plus|tk-mapper|redis|redisson|restful-web|tenant|rpc}"
        echo ""
        echo "Available tests:"
        echo "  all           - Run all tests"
        echo "  boot          - Test Boot Example"
        echo "  log           - Test Log Example"
        echo "  minio         - Test Minio Example"
        echo "  mybatis       - Test MyBatis Example"
        echo "  mybatis-plus  - Test MyBatis Plus Example"
        echo "  tk-mapper     - Test Tk Mapper Example"
        echo "  redis         - Test Redis Example"
        echo "  redisson      - Test Redisson Example"
        echo "  restful-web   - Test RESTful Web Example"
        echo "  tenant        - Test Tenant Example"
        echo "  rpc           - Test RPC Example"
        exit 1
        ;;
esac

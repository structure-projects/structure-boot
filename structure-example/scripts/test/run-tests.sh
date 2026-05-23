#!/bin/bash

# Test Runner - Runs all example module tests
# Executes all individual test scripts in sequence

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_warning() { echo -e "${YELLOW}[WARNING]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo "========================================="
echo "  Structure Example Test Runner"
echo "========================================="
echo ""

# Check if build has been done
check_build() {
    print_info "Checking if examples are built..."
    
    modules=(
        "structure-redis-example"
        "structure-restful-web-example"
    )
    
    all_built=true
    for module in "${modules[@]}"; do
        if [ ! -f "$PROJECT_ROOT/$module/target/${module}-1.0.1.jar" ]; then
            print_warning "Module $module not built. Building..."
            all_built=false
        fi
    done
    
    if [ "$all_built" = false ]; then
        print_info "Running build script first..."
        "$SCRIPT_DIR/build-examples.sh" all
        if [ $? -ne 0 ]; then
            print_error "Build failed. Please fix build issues first."
            exit 1
        fi
    fi
    
    print_success "All examples are built"
}

# Main test execution
run_tests() {
    check_build
    
    echo ""
    print_info "Starting test execution..."
    echo ""
    
    # Track test results
    total_tests=0
    passed_tests=0
    failed_tests=0
    
    # Test Redis
    echo ""
    print_info "========================================="
    print_info "Testing: Redis Example"
    print_info "========================================="
    if "$SCRIPT_DIR/test-redis.sh" test; then
        ((passed_tests++))
        print_success "Redis Example tests: PASSED"
    else
        ((failed_tests++))
        print_error "Redis Example tests: FAILED"
    fi
    ((total_tests++))
    
    # Test RESTful Web
    echo ""
    print_info "========================================="
    print_info "Testing: RESTful Web Example"
    print_info "========================================="
    if "$SCRIPT_DIR/test-restful-web.sh" test; then
        ((passed_tests++))
        print_success "RESTful Web Example tests: PASSED"
    else
        ((failed_tests++))
        print_error "RESTful Web Example tests: FAILED"
    fi
    ((total_tests++))
    
    # Summary
    echo ""
    echo "========================================="
    echo "  Test Summary"
    echo "========================================="
    print_info "Total tests: $total_tests"
    print_success "Passed: $passed_tests"
    if [ $failed_tests -gt 0 ]; then
        print_error "Failed: $failed_tests"
    fi
    echo ""
    
    if [ $failed_tests -eq 0 ]; then
        print_success "All tests passed! ✓"
        exit 0
    else
        print_error "Some tests failed"
        exit 1
    fi
}

# Show available tests
show_available_tests() {
    print_info "Available test modules:"
    echo ""
    for script in "$SCRIPT_DIR"/test-*.sh; do
        if [ -f "$script" ]; then
            name=$(basename "$script" .sh | sed 's/test-//')
            echo "  - $name"
        fi
    done
    echo ""
}

# Parse arguments
case "$1" in
    all)
        run_tests
        ;;
    list)
        show_available_tests
        ;;
    redis)
        "$SCRIPT_DIR/test-redis.sh" test
        ;;
    restful-web)
        "$SCRIPT_DIR/test-restful-web.sh" test
        ;;
    *)
        echo "Usage: $0 {all|list|redis|restful-web}"
        echo ""
        echo "Commands:"
        echo "  all          - Run all tests"
        echo "  list         - List available tests"
        echo "  redis        - Run Redis example tests"
        echo "  restful-web  - Run RESTful Web example tests"
        echo ""
        show_available_tests
        exit 1
        ;;
esac

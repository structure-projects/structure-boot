#!/bin/bash

# Quick Start Script - One command to build and test everything
# This script demonstrates the complete workflow

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

print_info() { echo -e "${BLUE}[INFO]${NC} $1"; }
print_success() { echo -e "${GREEN}[SUCCESS]${NC} $1"; }
print_error() { echo -e "${RED}[ERROR]${NC} $1"; }

echo ""
echo "========================================="
echo "  Structure Boot - Quick Start"
echo "  Build & Test Everything"
echo "========================================="
echo ""

# Step 1: Build
print_info "Step 1: Building all examples..."
echo ""

cd "$SCRIPT_DIR"
if ./build-examples.sh all; then
    print_success "✓ Build completed successfully"
    echo ""
else
    print_error "✗ Build failed"
    exit 1
fi

# Step 2: Test
echo ""
print_info "Step 2: Running all tests..."
echo ""

if ./run-tests.sh all; then
    print_success "✓ All tests passed"
    echo ""
    echo "========================================="
    echo "  🎉 Congratulations!"
    echo "  All examples built and tested successfully!"
    echo "========================================="
    echo ""
    print_info "To start individual services manually:"
    echo "  ./test-redis.sh start"
    echo "  ./test-restful-web.sh start"
    echo ""
    print_info "To run specific tests:"
    echo "  ./run-tests.sh redis"
    echo "  ./run-tests.sh restful-web"
    echo ""
    print_info "View logs:"
    echo "  cat logs/build-examples.log"
    echo "  cat logs/redis-example.log"
    echo ""
else
    print_error "✗ Some tests failed"
    echo ""
    print_info "Check logs for details:"
    echo "  cat logs/build-examples.log"
    echo "  cat logs/redis-example.log"
    echo ""
    exit 1
fi

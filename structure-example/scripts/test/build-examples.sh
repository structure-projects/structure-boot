#!/bin/bash

# Color output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
JAVA_HOME="${JAVA_HOME:-/Users/chuck/.sdkman/candidates/java/17.0.12-oracle}"
APP_USER="chuck"
LOG_DIR="$SCRIPT_DIR/logs"
mkdir -p "$LOG_DIR"

# Print colored message
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check Java version
check_java() {
    if [ -d "$JAVA_HOME" ]; then
        export JAVA_HOME
        export PATH="$JAVA_HOME/bin:$PATH"
        java -version >/dev/null 2>&1
        if [ $? -eq 0 ]; then
            JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
            print_success "Java version: $JAVA_VERSION"
            return 0
        fi
    fi
    
    print_error "Java 17 not found. Please set JAVA_HOME to JDK 17 path."
    return 1
}

# Build all examples
build_all_examples() {
    print_info "Building all example modules..."
    
    cd "$PROJECT_ROOT"
    
    # Build parent project first
    mvn clean install -DskipTests > "$LOG_DIR/build-examples.log" 2>&1
    
    if [ $? -eq 0 ]; then
        print_success "All example modules built successfully!"
        print_info "Build log: $LOG_DIR/build-examples.log"
        return 0
    else
        print_error "Failed to build example modules. Check log for details."
        print_error "Build log: $LOG_DIR/build-examples.log"
        return 1
    fi
}

# Build single module
build_module() {
    local module_name=$1
    print_info "Building module: $module_name"
    
    cd "$PROJECT_ROOT/$module_name"
    
    mvn clean package -DskipTests > "$LOG_DIR/build-${module_name}.log" 2>&1
    
    if [ $? -eq 0 ]; then
        print_success "Module $module_name built successfully!"
        return 0
    else
        print_error "Failed to build module $module_name"
        return 1
    fi
}

# Main menu
show_menu() {
    echo ""
    echo "========================================="
    echo "  Structure Example Test Scripts"
    echo "========================================="
    echo ""
    echo "1. Build all examples"
    echo "2. Build single module"
    echo "3. Exit"
    echo ""
    echo -n "Please select an option [1-3]: "
}

# Main program
main() {
    if ! check_java; then
        exit 1
    fi
    
    if [ $# -eq 0 ]; then
        show_menu
        read choice
        case $choice in
            1)
                build_all_examples
                ;;
            2)
                echo "Available modules:"
                ls -1 "$PROJECT_ROOT" | grep structure-
                echo -n "Enter module name: "
                read module_name
                build_module "$module_name"
                ;;
            3)
                print_info "Exiting..."
                exit 0
                ;;
            *)
                print_error "Invalid option"
                exit 1
                ;;
        esac
    else
        case $1 in
            all)
                build_all_examples
                ;;
            *)
                build_module "$1"
                ;;
        esac
    fi
}

main "$@"

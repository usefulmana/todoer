#!/bin/bash

# Default values
PROFILE="dev"
DEBUG_MODE=false
DEBUG_PORT=5005
COMMAND="run"  # Default command

# Function to print usage
print_usage() {
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  run    Run the application (default)"
    echo "  kill   Kill the running application"
    echo ""
    echo "Options:"
    echo "  -p, --profile     Specify profile (dev/prod)"
    echo "  -d, --debug       Enable debug mode"
    echo "  --debug-port      Specify debug port (default: 5005)"
    echo ""
    echo "Examples:"
    echo "  $0 run            # Run in dev mode"
    echo "  $0 run -p prod    # Run in prod mode"
    echo "  $0 run -d         # Run in debug mode"
    echo "  $0 kill           # Kill the application"
}

# Function to kill the application
kill_app() {
    echo "Killing Spring Boot application..."
    # Find PIDs of Java processes running Spring Boot
    PIDS=$(ps aux | grep "[j]ava.*spring-boot" | awk '{print $2}')
    if [ -z "$PIDS" ]; then
        echo "No Spring Boot application found running"
        return
    fi

    for PID in $PIDS; do
        echo "Killing process $PID..."
        kill -15 $PID
        if [ $? -eq 0 ]; then
            echo "Successfully killed process $PID"
        else
            echo "Failed to kill process $PID, attempting force kill..."
            kill -9 $PID
        fi
    done
}

# Parse command if provided
if [ $# -gt 0 ]; then
    case $1 in
        run|kill)
            COMMAND=$1
            shift
            ;;
        -h|--help)
            print_usage
            exit 0
            ;;
    esac
fi

# Parse remaining arguments for run command
while [[ $# -gt 0 ]]; do
    case $1 in
        -p|--profile)
            PROFILE="$2"
            shift 2
            ;;
        -d|--debug)
            DEBUG_MODE=true
            shift
            ;;
        --debug-port)
            DEBUG_PORT="$2"
            shift 2
            ;;
        *)
            echo "Unknown option: $1"
            print_usage
            exit 1
            ;;
    esac
done

# Execute command
case $COMMAND in
    run)
        # Print the configuration
        echo "Running with profile: $PROFILE"
        if [ "$DEBUG_MODE" = true ]; then
            echo "Debug mode enabled on port: $DEBUG_PORT"
        fi

        # Build the Gradle command
        if [ "$DEBUG_MODE" = true ]; then
            ./gradlew bootRunEnv \
                -Pprofile=$PROFILE \
                --debug-jvm \
                -Dorg.gradle.jvmargs="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT"
        else
            ./gradlew bootRunEnv -Pprofile=$PROFILE
        fi
        ;;
    kill)
        kill_app
        ;;
esac
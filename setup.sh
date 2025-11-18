#!/usr/bin/env bash
set -e

echo "Installing system dependencies required for Playwright browsers on Ubuntu..."

# Make apt-get non-interactive
export DEBIAN_FRONTEND=noninteractive

sudo apt-get update
sudo apt-get install -y \
    libevent-2.1-7 \
    libgstreamer-plugins-bad1.0-0 \
    libflite1 \
    gstreamer1.0-libav \
    libnss3 \
    libatk1.0-0 \
    libcups2 \
    libdrm2 \
    libxkbcommon0 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libgbm1 \
    libasound2t64 \
    libpangocairo-1.0-0 \
    libpango-1.0-0 \
    libgtk-3-0 \
    libx11-xcb1 \
    wget \
    curl \
    ca-certificates

echo "System dependencies installed."

# Install Playwright dependencies and browsers
echo "Installing Playwright dependencies..."
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

echo "Setup complete! You can now run the tests."

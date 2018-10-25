#!/bin/sh

FORGE_VERSION='1.12.2-14.23.0.2515'

wget https://files.minecraftforge.net/maven/net/minecraftforge/forge/${FORGE_VERSION}/forge-${FORGE_VERSION}-mdk.zip
unzip -n forge-${FORGE_VERSION}-mdk.zip
rm forge-${FORGE_VERSION}-mdk.zip
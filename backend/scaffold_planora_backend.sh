#!/usr/bin/env bash
set -euo pipefail

# Usage: bash scaffold_resources.sh
# Will prompt for REST API + CRUD for each resource.

resources=(
  auth
  users
  trips
  ai
  location
  collaboration
  files
  notifications
  checklists
  cache
  database
  storage
  integrations
)

for r in "${resources[@]}"; do
  echo "==> Generating $r"
  nest g resource "$r" --no-spec
done

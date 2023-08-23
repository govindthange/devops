#!/bin/bash

if [ $# -ne 3 ]; then
    echo "Usage: $0 <svn_repo_url>"
    exit 1
fi

svn_repo_url="$1"
svn_repo_user="$2"
svn_repo_pwd="$3"

last_commit_date=$(svn info "$svn_repo_url" --username "$svn_repo_user" --password "$svn_repo_pwd" | awk '/Last Changed Date:/ {print $4, $5}')

echo "Last commit date for $svn_repo_url: $last_commit_date"

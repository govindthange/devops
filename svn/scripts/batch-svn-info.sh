#!/bin/bash

input_csv="$1"
output_csv="$2"

# Check if both input arguments are provided
if [ -z "$input_csv" ] || [ -z "$output_csv" ]; then
    echo "Usage: $0 <input_csv_file> <output_csv_file>"
    exit 1
fi

# Print CSV header for the output
echo "svn_repo_url,Revision,Last_Changed_Author,Last_Changed_Rev,Last_Changed_Date,Last_Changed_Time" > "$output_csv"

# Read and process each line in the input CSV
while IFS=, read -r svn_repo_url svn_repo_user svn_repo_pwd; do
    svn_info_output=$(svn info "$svn_repo_url" --username "$svn_repo_user" --password "$svn_repo_pwd" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        revision=$(echo "$svn_info_output" | awk '/^Revision:/ { print $2 }')
        author=$(echo "$svn_info_output" | awk '/^Last Changed Author:/ { print $4 }')
        last_changed_rev=$(echo "$svn_info_output" | awk '/^Last Changed Rev:/ { print $4 }')
        last_changed_date_time=$(echo "$svn_info_output" | grep '^Last Changed Date:' | sed -E 's/^Last Changed Date: (.+) \(.+\)/\1/')
        
        echo "$svn_repo_url,$revision,$author,$last_changed_rev,$last_changed_date_time" >> "$output_csv"
    else
        echo "$svn_repo_url,Error fetching info" >> "$output_csv"
    fi
done < "$input_csv"

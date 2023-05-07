#!/usr/bin/bash

# Upper cases any file that matches "_.svg",
# "_" is a single character.

fPattern="^.\.svg"

for i in *; do
	if [[ $i =~ $fPattern ]]; then
		echo "$i"
		mv $i ${i^}
	fi
done
SELECT 'CREATE DATABASE scrabble'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'scrabble')\gexec
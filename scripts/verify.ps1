# Windows PowerShell 5.1+; safe local baseline verification.
$ErrorActionPreference = 'Stop'
$repoRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
Push-Location $repoRoot
try {
    foreach ($tool in @('java', 'docker', 'git')) {
        if (-not (Get-Command $tool -ErrorAction SilentlyContinue)) {
            throw "Missing tool: $tool. Install it and rerun this script."
        }
    }

    Write-Host 'Checking Java:'
    & java -version
    if ($LASTEXITCODE -ne 0) { throw 'Java failed.' }

    Write-Host 'Checking Docker Compose:'
    & docker compose version
    if ($LASTEXITCODE -ne 0) { throw 'Docker Compose plugin is unavailable.' }

    Write-Host 'Starting isolated development PostgreSQL on 127.0.0.1:5433...'
    & docker compose up -d --wait
    if ($LASTEXITCODE -ne 0) { throw 'Docker Compose startup failed. Check Docker Desktop and port 5433.' }

    $env:SPRING_DATASOURCE_URL = 'jdbc:postgresql://localhost:5433/employee_db'
    $env:SPRING_DATASOURCE_USERNAME = 'postgres'
    $env:SPRING_DATASOURCE_PASSWORD = 'postgres'

    Write-Host 'Running actual Maven tests...'
    & .\mvnw.cmd -B -ntp test
    if ($LASTEXITCODE -ne 0) { throw "Maven tests failed (exit code $LASTEXITCODE)." }

    Write-Host 'Baseline tests PASSED on this machine.'
} finally {
    Pop-Location
}


# 🚀 spAIk-server 실행 가이드 (Docker)

spAIk-project의 `spAIk_server` 폴더를 독립적으로 실행하는 가이드입니다.  
팀원 누구나 동일한 환경에서 백엔드 서버를 실행할 수 있도록 작성되었습니다.

---
## 사전 준비
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) 설치 및 실행
- Windows 환경일 경우 WSL2 기반 Docker 권장


## 실행 명령어 모음
```bash
# 1️⃣ GitHub에서 프로젝트 클론
git clone https://github.com/spAIk-GS/spAIk_server.git
cd spAIk_server

# 2️⃣ Gradle 빌드 (테스트 제외)
./gradlew clean build -x test

# 3️⃣ Docker Compose로 DB + 백엔드 실행
docker-compose -f docker-compose-backend.yml up -d

# 4️⃣ 실행 로그 확인
docker-compose -f docker-compose-backend.yml logs -f

# 5️⃣ 종료 시 (선택)
# docker-compose -f docker-compose-backend.yml down



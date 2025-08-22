
# 🚀 spAIk-server 실행 가이드 (Docker)

spAIk-project의 `spAIk_server` 폴더를 독립적으로 실행하는 가이드입니다.  
팀원 누구나 동일한 환경에서 백엔드 서버를 실행할 수 있도록 작성되었습니다.

---
## 사전 준비
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) 설치 및 실행
- Windows 환경일 경우 WSL2 기반 Docker 권장


## 실행 명령어 모음 (설명 포함, 1~6단계)
```bash
# 1️⃣ 레포지토리 최신화
# 로컬 백엔드 레포지토리를 원격 최신 상태로 업데이트
cd /path/to/spAIk_project/spAIk_server
git pull origin main  # main 브랜치 기준

# 2️⃣ Docker 이미지 빌드
# 현재 디렉토리의 Dockerfile을 기반으로 이미지를 생성
# -t spaik-server : 이미지 이름 지정
docker build -t spaik-server .

# 3️⃣ Docker 컨테이너 실행
# -d : 백그라운드 실행
# -p 8080:8080 : 호스트 8080 포트를 컨테이너 8080 포트에 매핑
# --name spaik-server : 컨테이너 이름 지정
docker run -d -p 8080:8080 --name spaik-server spaik-server

# 4️⃣ 서버 실행 확인
# 컨테이너 로그를 실시간으로 확인
docker logs -f spaik-server

# 5️⃣ 브라우저에서 접속
# 서버가 정상적으로 실행되면 브라우저에서 확인 가능
# URL: http://localhost:8080

# 6️⃣ 컨테이너 중지 및 삭제
# 필요 시 컨테이너를 중지하고 삭제 가능
docker stop spaik-server
docker rm spaik-server


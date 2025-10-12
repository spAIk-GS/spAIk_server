<div align="center">
  <img src="https://github.com/spAIk-GS/.github/blob/9ff7c8b89138369ee7b1f8398a2c941e6aa5a8fa/spAIk_logo.png" alt="조직 로고">
  
</div>

## ✨ 소개
##### spAIk팀은 팀 프로젝트 발표를 준비하며 겪었던 피드백 과정의 불편함에서 시작되었습니다. 많은 발표자가 자신의 발표를 객관적으로 평가하고, 특히 시선, 표정, 자세와 같은 비언어적 요소에 대해 체계적인 피드백을 받기 어렵다는 문제에 주목했습니다.

##### 이러한 문제의식은 AI 기술을 활용하여 발표 영상의 비언어적 요소를 정량적으로 분석하고, 시각적인 데이터와 함께 구체적인 피드백을 제공하는 웹 서비스 **spAIk**를 개발하는 원동력이 되었습니다. 
##### spAIk 팀의 목표는 발표자가 스스로의 강점과 약점을 명확히 파악하고, 더 효과적인 발표자로 성장할 수 있도록 돕는 것입니다.



## 🛠️ 주요 레포지토리 
##### 이 프로젝트는 여러 기술 스택과 기능을 분리하여 효율적인 협업과 관리를 위해 아래와 같은 주요 레포지토리로 구성되어 있습니다.
| 레포지토리 | 설명 |
|---|---|
| **spAIk_ai** | AI 기술을 활용하여 발표자의 음성 및 비디오를 분석하고, 정량적인 피드백을 제공하는 핵심 분석 엔진입니다. |
| **spAIk_client** | 발표 영상을 AI 엔진에 업로드하고 분석 결과를 받기 위한 독립형 클라이언트 응용 프로그램입니다. |
| **spAIk_server** | spAIk_ai 엔진을 API로 제공하고, 클라이언트로부터 받은 영상 데이터를 처리하여 분석을 요청 및 관리하며, 결과를 저장하는 백엔드 서버입니다. |


## 🛠️ 전체 실행 가이드

본 프로젝트의 각 레포지토리는 Docker 기반 컨테이너로 구성되어 있습니다.

제출한 압축 파일 내 각 폴더로 이동하여 명령어를 실행하거나, 별도의 폴더에서 GitHub로부터 클론 후 실행해도 무방합니다.
모든 서비스는 동시에 별도의 터미널에서 실행되어야 하며, 권장 실행 순서는 다음과 같습니다:

AI 서버 → Backend 서버 → Frontend 클라이언트

권장 실행 순서는 AI 서버 → Backend 서버 → Frontend 클라이언트이며, AI 초기화 시간과 DB 세팅을 고려한 순서입니다.
각 레포지토리별 실행 방법 및 필요한 명령어는 해당 폴더 내 README를 참고하시기 바랍니다.

**docker로 인한 실행에서 잘 작동하지 않는 경우 각 레포지토리의 readme 상단에 로컬 실행방법을 함께 첨부해두었습니다.**


## 💬 커뮤니티
-   **Notion**: [[Notion_spAIk](https://www.notion.so/spAIk-225b71be02228087b862e7b533505be9)]



# 🚀 Spring Boot 서버 실행 가이드

## 1️⃣ Git 클론
git clone https://github.com/es1206/spAIk_server.git
cd spAIk_server

## 2️⃣ 설치 확인 (선택)
git --version
java -version
./gradlew --version

## 3️⃣ 빌드 (테스트 제외)
./gradlew clean build -x test

## 4️⃣ 개발 서버 실행
./gradlew bootRun



==================================================
# 🚀 spAIk-server 실행 가이드 (Docker)

spAIk-project의 `spAIk_server` 폴더를 독립적으로 실행하는 가이드입니다.  
팀원 누구나 동일한 환경에서 백엔드 서버를 실행할 수 있도록 작성되었습니다.

---
## 사전 준비
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) 설치 및 실행

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



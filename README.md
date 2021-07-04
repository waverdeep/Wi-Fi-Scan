# 와이파이 핑거프린트 기반 데이터 수집을 위한 안드로이드 어플리케이션 개발
- 디바이스 주변 네트워크 장치들의 신호 (MAC, RSSI)를 스캔하는 어플리케이션
- 스캔한 정보들을 스마트폰에 CSV 형식으로 저장
- 혹은 웹서버와 HTTP - POST 통신하여 서버에 수집 데이터 저장

## 목적
- 와이파이 핑거프린트 기반 위치측위 솔루션 개발을 위한 데이터 수집

## 논문
- 와이파이 핑거프린트 기반 데이터 수집 방법 및 가공 연구
- Wi-Fi Fingerprint-base Data Colleection Method and Processing Research
- 한국정보통신학회 2019년도 춘계 학술대회
- [논문 보기](https://scienceon.kisti.re.kr/srch/selectPORSrchArticle.do?cn=NPAP12901305&dbt=NPAP)

## 사용된 권한
- ACCESS_COARSE_LOCATION
- ACCESS_WIFI_STATE
- ACCESS_NETWORK_STATE
- CHANGE_WIFI_STATE
- CHANGE_WIFI_MULTICAST_STATE
- WRITE_EXTERNAL_STORAGE
- READ_EXTERNAL_STORAGE
- INTERNET

## 사용 기법
- AsyncTask
- HttpUrlConnection
- WifiManager

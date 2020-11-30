<h1 align="center">
  <br>
  <img src="images/ic_main.png" alt="Markdownify" width="180"></a>
  <br>
  비대리
  <h3 align="center"> 비대리 - 비대면 강의를 쉽고 체계적으로!</h3>
  <br>
</h1>

## 사용 기술 및 라이브러리
- Android Studio 
- Kotlin
- lastAdapter - recyclerview adapter (https://github.com/nitrico/LastAdapter)
- Glide - image processing (https://github.com/bumptech/glide)
- Material Design - design theme, widgets (https://material.io)
- rxJava - reactivex programming (https://github.com/ReactiveX/RxJava)
- Jsoup - html web parser (https://jsoup.org)
- CircleProgress - circle indicator view (https://github.com/lzyzsd/CircleProgress)

## 주요 기능

- 강의를 온라인으로 수강할 수 있는 국민대학교 이캠퍼스 연동(http://ecampus.kookmin.ac.kr)
- <b>통합로그인</b> 지원
- <b>세션 유지</b> 지원 (로그인 세션 만료시까지 로그인 상태 유지)
- 앱 실행시마다 모든 정보는 <b>실시간 정보</b>임을 보장
- 강의별 <b>오늘 할 일</b> / <b>앞으로 해야할 일</b>을 한번에 볼수 있는 UI
- 오늘 할 일들은 시간이 되면 <b>자동으로 Notification(알림)</b> 보내줌
- 강의실을 통해 각 수강과목들의 출석률을 볼 수 있음

## 구현 내용

### 아이콘
<img src="images/app_icon_preview.png" alt="Markdownify" width="100"></a>
- 커스텀 round 앱 아이콘

### 스플래쉬 화면
<img src="images/view_splash.png" alt="Markdownify" width="300"></a>
- 앱이 시작하면 보여짐
- 간단하게 앱 정보를 보여주는 스플래쉬 화면

### 로그인 화면
<img src="images/view_login.png" alt="Markdownify" width="300"></a>
- 국민대학교 통합로그인 아이디와 비밀번호로 로그인 하는 폐이지
- Ecampus로부터 로그인 세션 토큰이 포함된 쿠키를 받아옴

<img src="images/view_login_live.gif" alt="Markdownify" width="300"></a>
- 만약 로그인 세션이 살아있다면 앱 재시작시 해당 페이지 자동으로 스킵함

### 홈 화면
<img src="images/view_home.png" alt="Markdownify" width="300"></a>
- 처음으로 뜨는 화면
- 상단바에는 오늘의 날짜가 보여짐
- 오늘 할 일 그리고 예정된 할 일을 한번에 볼 수 있음
- 강의 타입(ZOOM 화상강의, 동영상 녹화강의, 과제, 파일)별로 직관적인 아이콘

<img src="images/view_home_day_multiple.png" alt="Markdownify" width="300"></a>
- 예정된 할 일에서 같은 날짜에 할 일이 여러개라면 날짜를 중복해서 보여주지 않고 시간만 보여줌 (UI/UX 적 관점에서 직관성 증가)

<img src="images/view_home_live.gif" alt="Markdownify" width="300"></a>
- 할 일 클릭하면 해당 할 일로 손쉽게 이동

### 알림 서비스
<img src="images/notification_live.gif" alt="Markdownify" width="300"></a>
- 오늘의 할일 강의나 과제 제출 마감 시간이 되면 자동으로 알림 전송
- 클릭하면 앱이 열려서 쉽게 할 일을 보고 열 수 있음
- 백그라운드 서비스로 동작하기 때문에 다른일을 하거나 잠금상태여도 알림 도착

### 강의실
<img src="images/view_lecture_live.gif" alt="Markdownify" width="300"></a>
- 현 학기에 수강중인 강의를 한눈에 볼 수 있음
- 클릭하면 해당 과목으로 손쉽게 이동
- 각 강의별로 출석률을 제공함. 단, 현재 통합인증 SSO 인증 업그레이드로 인하여 데이터 접근이 막힘 (추후 대응 예정)

### 시간표

<img src="images/timetable.png" alt="Markdownify" width="300"></a>

- 수강중인 강의의 시간표를 보여줌
- 날짜 정보를 이용해 자동으로 학기를 나타냄
- 각 강의의 강의명, 교수명, 강의실 위치를 보여줌
- 다만 현재 통합인증 SSO 인증 업그레이드로 인하여 데이터 접근이 불가함. 



<img src="images/timetable_add_dialog.png" alt="Markdownify" width="300"></a>

- 상단 왼쪽 + 버튼 클릭 시 다이얼로그 팝업창 뜸
- 팝업창에 정보 작성
- 시간 선택은 spinner 를 활용하여 편리하도록 구현



<img src="images/timetable_add_dialog2.png" alt="Markdownify" width="300"></a>

- 이후 확인버튼 클릭 시 시간표에 강의가 추가됨



<img src="images/timetable_add.png" alt="Markdownify" width="300"></a>

- 강의가 시간표에 추가된 모습



<img src="images/timetable_day.png" alt="Markdownify" width="300"></a>

- 올바르지 않은 정보를 확인하여 추가할 수 있도록 함
- 사진상의 내용은 올바른 요일을 선택하지 않음



### 캘린더

<img src="images/view_calendar.png" alt="Markdownify" width="300"></a>

- ecampus 일정을 연동하여 캘린더에 표시함
- 캘린더에 라벨로 일정 표시 가능
- 다만 현재 일부 기능 미구현

## 한계점

프로젝트 시작 후 통합정보시스템 업데이트가 이루어지게 되면서, 당초 예상했던 방향과 프로젝트 계획이 틀어지게 되어 로그인등의 문제가 발생함.

ecampus와 kcard 양쪽에서 정보를 얻어와야 정상적인 서비스 제공이 가능하나, 통합정보시스템이 업데이트 되면서 sso 연결 등 보안의 업데이트로 데이터 파싱이 불가능해져 일부 기능을 완성하지 못하였음.

추후 api등을 요청하여 앱을 발전시켜보고자 함.

# ClipStudio
#### 동영상 관리 및 동영상 조회수 기반 수익 정산과 조회 기능을 제공하는 프로그램입니다.

- 동영상 업로드 및 재생 API
- 기간별 동영상 수익 통계 조회 API
    - 기간별 동영상 조회수 기반 수익 통계
    - 기간별 광고 조회수 기반 수익 통계
    - 가간별 동영상 및 광고 조회수 기반 총 수익 통계 제공

## 개발 환경 및 사용 기술

- Java21
- docker
- mariaDB
- Spring Boot 3.2.5
    - Spring Security
    - Spring Batch
- JPA hibernate
- JDBC template

## API 명세

### 동영상 등록 API

새로운 동영상을 업로드합니다.

| url | method | request | response |
| --- | --- | --- | --- |
| /api/videos | POST | VideoUploadDto | VideoDto |


<details>
<summary>description</summary>
<div markdown="1">


- request(Content-type: application/json)
    ```json
    

    {
    	"title": "New Video!",
    	"description": "This is a new video for testing.",
    	"url": "http://some-url"
    }
    ```
    
- response
    
    ```json
    
    {
    	"number": 1,
    	"uploaderNumber": 10,
    	"createdDate": "2024-05-19",
    	"title": "New Video!",
    	"description": "This is a new video for testing.",
    	"durationSec": 123,
    	"url": "http://some-url"
    }
    ```
    
- 서버 내부 동작 및 exception
    
    `VideoUploadDto`에 동영상 정보를 담아 요청을 전송하면 서버는 동영상을 새로 등록하고 로그인 사용자를 동영상 업로더로 설정한다. 
    

</div>
</details>


### 동영상 재생 API

동영상을 특정 위치까지 재생합니다.

| url | method | request | response |
| --- | --- | --- | --- |
| api/player/{videoNumber} | POST  | {videoNumber}, VideoPlayDto | WatchHistoryDto |

<details>
<summary>description</summary>
<div markdown="1">

- request(Content-type: application/json)
    
    ```json
    {
    	"videoStoppedSec": 100
    }
    ```
    
- response 예시
    
    ```json
    
    {
    	"userEmail": "user@email-address.com",
    	"videoNumber": 1,
    	"videoStoppedSec": 100
    }
    ```
    
- 서버 내부 동작 및 exception
    
    `VideoPlayDto`에 동영상의 재생 멈춤 시점을 담아 요청을 전송하면 서버는 `{videoNumber}` 로 찾은 동영상에 대한 로그인 사용자의 시청 기록을 새로 생성하거나 업데이트한다. 시청 기록이 새로 생성될 때 동영상 조회수가 1 증가하고, 기존 시청 기록이 업데이트될 때는 동영상 조회수가 증가하지 않는다. 재생 멈춤 시점이 동영상 길이보다 크다면 *exception*이 발생한다.
    

</div>
</details>



### 일일 수익 조회 API

동영상의 특정 날짜 하루 동안의 수익을 조회합니다.

| url | method | request | response |
| --- | --- | --- | --- |
| api/profit/day/{date} | GET  | {date} | List<DailyProfitDto> |

<details>
<summary>description</summary>
<div markdown="1">
- response 예시
    
    ```json
    [
    	{
    		"videoNumber": "1",
    		"date": "2024-05-01",
    		"videoProfit": 100500.3,
    		"advertisementProfit": 207000.0,
    		"totalProfit": 307500.3
    	},
    		{
    		"videoNumber": "120",
    		"date": "2024-05-01",
    		"videoProfit": 40890.1,
    		"advertisementProfit": 70003.4,
    		"totalProfit": 110893.5
    	}
    ]
    ```
    
- 서버 내부 동작 및 exception
    
    url에 동영상 수익을 조회하려는 날짜를 담아 요청을 전송하면 서버는 로그인 사용자가 올린 모든 동영상의 해당 날짜 하루 동안의 수익을 List 형태로 반환한다. 동영상 조회수 기반의 수익과 광고 조회수 기반의 수익을 각각 조회할 수 있으며, 이 둘을 합한 총 수익도 조회할 수 있다. 로그인 사용자가 업로드한 동영상이 없다면 빈 리스트를 반환하고, 정산 작업이 진행 중인 날짜 또는 미래의 날짜가 요청되면 *exception*이 발생한다.
    

</div>
</details>



### 기간별 수익 조회 API

동영상의 특정 날짜로부터 일정 기간의 수익을 조회합니다.

| url | method | request | response |
| --- | --- | --- | --- |
| api/profit/week/{date} | GET  | {date} | List<WeeklyProfitDto> |
| api/profit/month/{date} | GET  | {date} | List<MonthlyProfitDto> |
| api/profit/year/{date} | GET  | {date} | List<YearlyProfitDto> |


<details>
<summary>description</summary>
<div markdown="1">

- response 예시
    
    ```json
    [
    	{
    		"videoNumber": "1",
    		"startDate": "2024-05-01",
    		"endDate": "2024-05-31",
    		"videoProfit": 100500.3,
    		"advertisementProfit": 207000.0,
    		"totalProfit": 307500.3
    	},
    		{
    		"videoNumber": "120",
    		"date": "2024-05-01",
    		"endDate": "2024-05-31",
    		"videoProfit": 40890.1,
    		"advertisementProfit": 70003.4,
    		"totalProfit": 110893.5
    	}
    ]
    ```
    
- 서버 내부 동작 및 exception
    
    url에 동영상 수익을 조회하려는 날짜를 담아 요청을 전송하면 서버는 로그인 사용자가 올린 모든 동영상의 해당 날짜로부터 [기간] 동안의 수익을 List 형태로 반환한다. 동영상 조회수 기반의 수익과 광고 조회수 기반의 수익을 각각 조회할 수 있으며, 이 둘을 합한 총 수익도 조회할 수 있다. 로그인 사용자가 업로드한 동영상이 없다면 빈 리스트를 반환하며, 정산 작업이 진행 중인 날짜 또는 미래의 날짜가 요청되면 *exception*이 발생한다. 요청된 날짜가 현재로부터 [기간] 이내라면, 조회 가능한 기간의 데이터만 반환한다.
    

</div>
</details>

## 프로젝트 구조
- **ClipStudio(multi-module)**
    - API module
    - Batch module

- **DummyDataGenerator**
    - JDBC template으로 DML 구현
    - 사용자 데이터, 동영상 데이터 랜덤 생성
    - 랜덤 사용자의 랜덤 동영상 시청 기록 업데이트

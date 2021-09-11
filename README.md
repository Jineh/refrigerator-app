# Refrigerator-app


## 프로젝트 소개
#### 텐서플로우를 이용한 식품 레시피 추천
우리는 매일 냉장고를 사용하지만, 냉장고에 어떤 식재료가 있는지 유통기한은 얼마 남았는지 관리하는 것은 매우 번거롭다.
식재료를 사놓고 유통기한이 지날 때까지 사용하지 않고 버리게 되는 경우도 빈번하다. 따라서, 사용자의 냉장고에 있는 식재료를 관리하고 이를 바탕으로 레시피를 추천하는 어플을 제안하고자 한다.
<br/><br/>

## 프로젝트 구조
<img src="https://user-images.githubusercontent.com/58362196/132942422-e0b7675c-c549-48c2-8ec0-17a3af11edf6.jpg" width="70%" height="70%">
<br/><br/>

## 프로젝트 기능

* 앱 실행 시 로그인/ 회원가입 화면

<img src="https://user-images.githubusercontent.com/58362196/132943036-065b9434-1a3f-4726-b86a-61605aed3205.png" width="70%" height="70%">
<br/>

* navigation bar 메뉴에서 장바구니, 이용방법, 설정  
  -설정에서 사용자 고유코드 확인하고 코드를 통해 다른 사용자와 냉장고를 공유할 수 있다.

<img src="https://user-images.githubusercontent.com/58362196/132943060-eeb93c06-6340-4e1d-8d73-01b7d3f39eaf.png" width="70%" height="70%">
<br/>

-장바구니에서 todo 리스트 형식으로 식재료 추가, 삭제할 수 있다. 식재료 구매시 장바구니에서 식품을 선택하여 냉장고에 추가할 수 있다.

<img src="https://user-images.githubusercontent.com/58362196/132943149-8023b7cb-1771-46bd-88c6-c59b73b48c66.png" width="70%" height="70%">
<br/>

  -이용방법을 이미지를 통해 이해하기 쉽게 설명

<img src="https://user-images.githubusercontent.com/58362196/132943183-1c420f8e-a8c3-482b-be38-922972f1a0ab.png" width="70%" height="70%">
<br/>

* 식재료 추가하기 

  -식재료를 검색하여 직접 추가

<img src="https://user-images.githubusercontent.com/58362196/132943273-d2e4bbeb-e576-4285-ad44-c11c64121800.png" width="70%" height="70%">
<br/>

   -식재료를 카메라로 인식하여 추가

<img src="https://user-images.githubusercontent.com/58362196/132943287-17720bbe-8350-426d-83e1-10194a11a0cf.png" width="70%" height="70%">
<br/>

* 식재료들의 유통기한을 입력하면 알림을 등록하여 하루 전에 알림

<img src="https://user-images.githubusercontent.com/58362196/132943327-5db252c4-3f76-46de-bd39-7c64d3509c7c.png" width="70%" height="70%">
<br/>

* 메인 화면에서 등록한식재품들을 확인(냉장고 속 식재료들을 보여줌)
 
<img src="https://user-images.githubusercontent.com/58362196/132943342-8a7214b6-4cbf-4a90-a634-ab29fdd0042a.png" width="70%" height="70%">
<br/>

* 레시피 검색 기능을 통해 원하는 식재료들을 조합하여 레시피를 보여줌
 
<img src="https://user-images.githubusercontent.com/58362196/132943368-25593782-9c54-4fae-ab9f-14b5d3c81027.png" width="70%" height="70%">
<br/><br/>

## 개발 환경
| 구분 | 상세내용 |
| :---: | :---: |
| `android` | 안드로이드 버전 9 이상을 타겟 |
| `firebase` | 사용자, 식재료 관리 |
| `tensorflow` | TensorFlow Object Detection API, MobileNet V2 |
| `web crawling` | BeautifulSoup으로 이미지 데이터 셋 크롤링, JSoup으로 레시피 크롤링 |
| `AES-256` | AES 알고리즘으로 사죵자 고유코드 암호화, 복호화 |

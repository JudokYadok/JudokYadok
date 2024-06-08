# 📖 주독야독 (晝讀夜讀)
<div align="center">
  
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/00ac4d9e-0a3a-4e58-9de7-2132c3971d3c)<br>
<h3>기획 및 개발 기간 : 2024.02.13 ~ 2024.05.24</h3>
</div>
<br>

## 📚 목차

- [📚 목차](#-목차)
- [💡 서비스 소개](#-서비스-소개)
- [🛠 기술 스택](#-기술-스택)
- [👩‍💻 팀원 소개](#-팀원-소개)
- [🖥 주요 기능](#-주요-기능)
- [📋 서비스 개요](#-서비스-개요)
  
<br />

## 💡 서비스 소개

<b>'주독야독'</b>은 <b>GPT-4</b> 모델을 사용해 <b>사용자가 원하는 지문의 수능 국어 대표 출제 유형별 문제 및 지문 요약을 생성해주는 서비스</b>입니다.<br>
서비스 이름은 낮에는 농사하고 밤에는 글을 읽는다는 사자성어 '주경야독'을 변형한 말로, 밤낮으로 국어 독서(비문학) 파트를 공부하자는 의미를 담고 있습니다.

<br />

## 🛠 기술 스택

<table>
    <thead>
        <tr>
            <th>분류</th>
            <th>기술 스택</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>
                  <p>BE</p>
            </td>
            <td>
                  <img src="https://img.shields.io/badge/Node.js-5FA04E?style=flat&logo=Node.js&logoColor=white"/>
                  <img src="https://img.shields.io/badge/Express.js-000000?style=flat&logo=express&logoColor=white"/>
                  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>
                  <img src="https://img.shields.io/badge/MariaDB-003545?style=flat&logo=mariadb&logoColor=white"/>
            </td>
        </tr>
        <tr>
            <td>
                <p>FE</p>
            </td>
            <td>
              <img src="https://img.shields.io/badge/EJS-B4CA65?&logo=ejs&logoColor=000000">
              <img src="https://img.shields.io/badge/Android Studio-3DDC84?logo=androidstudio&logoColor=white">
            </td>
        </tr>
       <tr>
            <td>
                  <p>AI</p>
            </td>
            <td>
                  <img src="https://img.shields.io/badge/python-3776AB?style=flat&logo=python&logoColor=white"/>
                  <img src="https://img.shields.io/badge/openai-00B388?style=flat&logo=openai&logoColor=white"/>
            </td>
        </tr>
        <tr>
            <td>
                <p>협업</p>
            </td>
            <td>
                <img src="https://img.shields.io/badge/Notion-000000?logo=Notion">
                <img src="https://img.shields.io/badge/Figma-F24E1E?logo=Figma&logoColor=ffffff">
                <img src="https://img.shields.io/badge/Discord-5865F2?logo=Discord&logoColor=white">
            </td>
        </tr>
    </tbody>

</table>

<br />

## 👩‍💻 팀원 소개

  <table>
    <tr>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/143929569?v=4" width="160"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/104813592?v=4" width="160"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/141158150?v=4" width="160"></td>
      <td align="center"><img src="https://avatars.githubusercontent.com/u/90694063?v=4" width="160"></td>
    </tr>
    <tr>
      <td align="center"><b>박민정</b>(AI)</td>
      <td align="center"><b>구연우</b>(BE)</td>
      <td align="center"><b>김수정</b>(BE)</td>
      <td align="center"><b>박혜정</b>(FE)</td>
    </tr>
    <tr>
      <td align="center"><a href="https://github.com/MiinJng" target="_blank">@MiinJng</a></td>
      <td align="center"><a href="https://github.com/nuyeo" target="_blank">@nuyeo</a></td>
      <td align="center"><a href="https://github.com/soojxng" target="_blank">@soojxng</a></td>
      <td align="center"><a href="https://github.com/yummpotato" target="_blank">@yummpotato</a></td>
    </tr>
  </table>

<br />

## 🖥 주요 기능

### AI 문제 생성

> 사용자가 선택한 지문을 바탕으로 국어 기출 출제 대표유형 4가지 중 사용자가 선택한 유형의 문제를 GPT-4로 생성합니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/6dbef693-b2f1-415d-a1ad-8fab72ea22a4)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/545aa001-aa15-4da9-b10e-687629ff64b4)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/7a8f0214-c660-4a45-9142-036c3d190fe0)

<br />

### AI 지문 요약

> 사용자가 응시한 문제를 채점할 때, 해당 지문을 GPT-4로 요약한 지문 요약본도 함께 제공합니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/660bce95-981d-4809-b159-361d1dc65ff1)

<br />

### 사용자 지문 추가

> 사용자가 공부하고 싶은 지문을 추가하고, 이를 통해 문제를 생성할 수 있습니다. 지문 카테고리 설정 및 카테고리별 조회가 가능합니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/b8b25425-952e-413d-a0d6-078202dc1b8f)

<br />

### 기출 지문 제공

> 학습 편의를 위해 최근 3개년도 평가원 기출 지문을 기본으로 제공하며, 이를 통해 문제를 생성할 수 있습니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/6e08ee71-dbfc-4ad3-b5d0-eb9611161ccd)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/457c53ce-b06f-4ca1-a0fc-3cf57872eeb7)

<br />

### 문제 풀이 시간 측정용 스톱워치

> 사용자가 문제를 푸는데 얼마나 소요되는지 손쉽게 측정할 수 있도록 문제 응시 화면에서 스톱워치 기능을 제공합니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/255b9b35-a040-43c6-9c0a-4c59445fabb9)

<br />

### 문제 및 메모 저장

> 응시한 문제 및 사용자가 입력했던 답을 저장하고 나중에 복습할 수 있습니다. 풀면서 어려웠던 부분이나 새로 알게 된 부분을 기록할 수 있도록 메모 저장 기능도 함께 제공합니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/6a52f10a-b8da-45ea-8391-4ab1185eff02)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/d2f2b034-2f10-4210-b52a-541560a1901f)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/ba2229c1-56d9-44fe-ae8b-386a3cbfa67d)

<br />

### 소셜 로그인

> 카카오 로그인 및 자동 로그인을 지원합니다.
 
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/b798faa2-5e2b-48fc-a53a-632ee7530e4f)

<br />

### 수능 디데이 확인

> 마이페이지에서 자신이 설정한 수능 날짜의 디데이를 확인할 수 있습니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/6964963b-c442-4478-88be-e0c318183e24)

<br />

### 서비스 피드백 작성

> 만들어진 문제 및 전체 서비스에 관한 피드백을 작성할 수 있으며, 관리자가 이를 받아 사용자별 피드백을 확인하고 개선 사항에 반영할 수 있습니다.

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/9140ce6b-77c2-4c1a-9937-2047048bb79d)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/9525799f-2551-4d0c-95ef-262c56be6f6b)

<br />

## 📋 서비스 개요

![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/65349ccc-85a1-40a1-9436-cd4dc3bcc050)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/d4469e27-4f31-4d94-933d-4170eb8b0654)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/eb4c34a7-6924-4f08-9ad7-faaa75bc1eac)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/4b307dfe-a396-4bb8-b69a-f596add8bb59)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/39afd570-48b1-4cdf-9fc6-1bce0ab9c2ba)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/ac7209d1-184f-4125-bf40-1e32fc1591a6)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/de3e9fa7-0297-4566-8819-b80d68e58c4c)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/b3fc4421-af20-40ea-979f-2ed362fe4331)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/f30edbb4-1672-4296-9705-93a3c9ad545a)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/8fda6516-5e9b-44c4-b7fd-1da7fee8b3b3)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/18badfbb-cabb-421e-b94b-ef3c9fc23629)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/b1ccdb1b-345f-4fec-b73d-888f7a8d69c3)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/e9c6a455-8a17-4194-9654-ee8c4a2bdfea)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/bce81737-1dc2-424a-b853-9ac0d00e0005)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/78c192af-66b1-492b-89f9-503ea282aada)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/e3e869f9-c893-4a21-ab88-c985988cba52)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/c48bd537-2a3d-4bf2-b84c-8b3d760b586d)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/246f364f-0b3e-47a6-be00-bc7665ccb1e6)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/f4e2e601-5a88-40d8-a818-176c579ba698)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/2cd5fb4f-3f2c-43b4-91a6-cfa9ca95eb1c)
![image](https://github.com/nuyeo/JudokYadokTemp/assets/104813592/3cbff642-0769-4992-8719-01994e97b698)




















<br />

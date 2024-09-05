# -*- coding: utf-8 -*-

import sys
import json
from langchain_community.document_loaders import TextLoader
from langchain_openai import ChatOpenAI
from langchain.prompts import ChatPromptTemplate
from langchain.callbacks import StreamingStdOutCallbackHandler
from langchain.text_splitter import CharacterTextSplitter
from langchain.schema import BaseOutputParser, output_parser

text = sys.argv[1]

class JsonOutputParser(BaseOutputParser):
    def parse(self, text):
        text = text.replace("```", "").replace("json", "")
        return json.loads(text)


output_parser = JsonOutputParser()

llm = ChatOpenAI(
    temperature=0.1,
    model="gpt-4-1106-preview",
    # streaming=True,
    # callbacks=[StreamingStdOutCallbackHandler()]
)


def split_text_with_overlap(text, chunk_size, chunk_overlap):
    length = len(text)
    chunks = []
    start = 0
    
    while start < length:
        # 청크의 끝 위치 계산
        end = start + chunk_size
        
        # 청크 추출
        chunk = text[start:end]
        chunks.append(chunk)
        
        # 다음 청크의 시작 위치를 설정 (중복 포함)
        start += (chunk_size - chunk_overlap)
    
    return chunks

chunk_size = 600
chunk_overlap = 100

# 함수를 호출하여 청크 리스트를 받음
chunk_list = split_text_with_overlap(text, chunk_size, chunk_overlap)

question_prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                """
    너는 교사 역할을 하는 유용한 보조자야.

    다음 문맥에 기초하여 3개의 질문을 만들어서 주어진 텍스트에 대한 사용자의 지식을 테스트해줘.

    각 질문에는 5개의 예시가 있어야 하며, 그 중 4개는 오답이고 1개는 정답이야.

    (o)를 사용해서 정답을 표시해줘.

    문제의 유형은 "윗글의 내용과 일치하는 것은?", "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?", "윗글에 대한 이해로 적절하지 않은 것은?", "'aaa'과 'bbb'를 이해한 내용으로 적절한 것은?" 을 반드시 한 문제씩 생성해서 총 3문제를 생성하줘.

    "윗글의 내용과 일치하는 것은?"의 문제는 텍스트에 대한 내용을 이해했는지에 대한 문제야.
    "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?"의 문제는 주어진 텍스트에 등장하는 내용의 전개 방식을 묻는 문제야.
    "윗글에 대한 이해로 적절하지 않은 것은?"의 문제는 주어진 텍스트에 등장하는 내용을 읽고, 사용자가 내용을 이해했는지 질문하는 문제야.
    # "'aaa'과 'bbb'를 이해한 내용으로 적절한 것은?"의 문제는 'aaa'는 첫 번째 핵심 키워드를 의미하고 'bbb'는 또 다른 핵심 키워드를 넣어서 주어진 텍스트에 등장하는 핵심 키워드 2개에 대한 내용을 이해했는지 질문하는 문제야.

         
    Question: 윗글의 내용과 일치하는 것은?
    Answer: 
    1. 차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.
    2. 외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.
    3. 차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.(o)
    4. 영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.
    5. 위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.
    
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
   Answer: 
    1. 특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.
    2. 특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.
    3. 특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.
    4. 특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.(o)
    5. 특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.

    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.(o)
    2. 사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.
    3. 인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.
    4. 한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.
    5. 천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.

   

    너의 차례야!
         
    Context: {context}
""",
            )
        ]
)

question_chain = question_prompt | llm

formatting_prompt = ChatPromptTemplate.from_messages(
    [
        (
            "system",
            """
    너는 강력한 포맷 알고리즘이야.
    시험 문제의 형식을 JSON 형식으로 포맷해줘.
    (o) 표시가 있는 1개의 보기만이 정답이야.

    Example Input:

    Question: 윗글의 내용과 일치하는 것은?
    Answer: 
    1. 차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.
    2. 외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.
    3. 차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.(o)
    4. 영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.
    5. 위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.
    
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
   Answer: 
    1. 특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.
    2. 특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.
    3. 특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.
    4. 특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.(o)
    5. 특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.
    

    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.(o)
    2. 사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.
    3. 인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.
    4. 한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.
    5. 천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.



    Example Output:

    ```json
    {{ "questions": [
            {{
                "question": "윗글의 내용과 일치하는 것은?",
                "answers": [
                        {{
                            "number": 1,
                            "answer": "차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.",
                            "correct": false
                        }},
                        {{
                            "number": 2,
                            "answer": "외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 3,
                            "answer": "차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.",
                            "correct": true
                        }},
                        {{
                            "number": 4,
                            "answer": "영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 5,
                            "answer": "위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.",
                            "correct": true
                        }},
                        {{
                            "number": 5
                            "answer": "특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글에 대한 이해로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.",
                            "correct": true
                        }},
                        {{
                            "number": 2
                            "answer": "사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.",
                            "correct": false
                        }},
                        {{
                            "number": 5
                            "answer": "천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.",
                            "correct": false
                        }}
                ]
            }}
        ]
     }}
    ```

    너의 차례야!

    Context: {context}


            """,

        )
    ]
)

formatting_chain = formatting_prompt | llm

chain = {"context": question_chain} | formatting_chain | output_parser

result = chain.invoke({"context": chunk_list})

print(json.dumps(result, ensure_ascii=False, indent=2))

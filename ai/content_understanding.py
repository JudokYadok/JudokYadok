# -*- coding: utf-8 -*-

import json
import sys
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

    문제의 유형은 주어진 텍스트에 등장하는 내용을 읽고, 사용자가 내용을 이해했는지 질문하는 문제야.

    각 질문은 "윗글에 대한 이해로 적절하지 않은 것은?"이나 "윗글에서 알 수 있는 내용으로 적절하지 않은 것은?"이라고 통일해줘.


         
   문제 예시
         
    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.
    2. 사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.
    3. 인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.
    4. 한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.
    5. 천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.(o)
         
    Question: 윗글에서 알 수 있는 내용으로 적절하지 않은 것은?
    Answer: 
    1. 계약상의 채권은 계약이 성립하면 추가 합의가 없어도 발생 하는 것이 원칙이다.
    2. 재화나 서비스 제공을 대상으로 하는 권리 외에 다른 형태의 권리도 존재한다.
    3. 예약상 권리자는 본계약상 권리의 발생 여부를 결정할 수 있다.(o)
    4. 급부가 이행되면 채무자의 채권자에 대한 채무가 소멸된다.
    5. 불법행위 책임은 계약의 당사자 사이에 국한된다.
         
    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 자연 영상은 모델링과 렌더링 단계를 거치지 않고 생성된다.
    2. 렌더링에서 사용되는 물체 고유의 표면 특성은 화솟값에 의해 결정된다.
    3. 물체의 원근감과 입체감은 관찰 시점을 기준으로 구현한다.
    4. 3D 영상을 재현하는 화면의 해상도가 높을수록 연산 양이 많아진다.(o)
    5. 병목 현상은 연산할 데이터의 양이 처리 능력을 초과할 때 발생한다.
         
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

    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.
    2. 사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.
    3. 인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.
    4. 한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.
    5. 천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.(o)
         
    Question: 윗글에서 알 수 있는 내용으로 적절하지 않은 것은?
    Answer: 
    1. 계약상의 채권은 계약이 성립하면 추가 합의가 없어도 발생 하는 것이 원칙이다.
    2. 재화나 서비스 제공을 대상으로 하는 권리 외에 다른 형태의 권리도 존재한다.
    3. 예약상 권리자는 본계약상 권리의 발생 여부를 결정할 수 있다.(o)
    4. 급부가 이행되면 채무자의 채권자에 대한 채무가 소멸된다.
    5. 불법행위 책임은 계약의 당사자 사이에 국한된다.
         
    Question: 윗글에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 자연 영상은 모델링과 렌더링 단계를 거치지 않고 생성된다.
    2. 렌더링에서 사용되는 물체 고유의 표면 특성은 화솟값에 의해 결정된다.
    3. 물체의 원근감과 입체감은 관찰 시점을 기준으로 구현한다.
    4. 3D 영상을 재현하는 화면의 해상도가 높을수록 연산 양이 많아진다.(o)
    5. 병목 현상은 연산할 데이터의 양이 처리 능력을 초과할 때 발생한다.
         

    Example Output:

    ```json
    {{ "questions": [
            {{
                "question": "윗글에 대한 이해로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1,
                            "answer": "과정 이론은 물리적 세계의 테두리 안에서 인과를 해명하는 이론이다.",
                            "correct": false
                        }},
                        {{
                            "number": 2,
                            "answer": "사회 규범 위반과 처벌 당위성 사이의 인과 관계는 표지의 전달로 설명되기 어렵다.",
                            "correct": false
                        }},
                        {{
                            "number": 3,
                            "answer": "인과가 과학적 세계관과 부합하지 않는다고 생각하는 철학 자가 근대 이후 서양에 나타났다.",
                            "correct": false
                        }},
                        {{
                            "number": 4,
                            "answer": "한대의 재이론에서 전제된 하늘은 음양의 변화에 반응하지 않지만 경고를 하는 의지를 가진 존재였다.",
                            "correct": false
                        }},
                        {{
                            "number": 5,
                            "answer": "천문학의 발달에 따라 일월식이 예측 가능해지면서 송대에는 이를 설명 가능한 자연 현상으로 보는 경향이 있었다.",
                            "correct": true
                        }},
                ]
            }},
                        {{
                "question": "윗글에서 알 수 있는 내용으로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "계약상의 채권은 계약이 성립하면 추가 합의가 없어도 발생 하는 것이 원칙이다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "재화나 서비스 제공을 대상으로 하는 권리 외에 다른 형태의 권리도 존재한다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "예약상 권리자는 본계약상 권리의 발생 여부를 결정할 수 있다.",
                            "correct": true
                        }},
                        {{
                            "number": 4
                            "answer": "급부가 이행되면 채무자의 채권자에 대한 채무가 소멸된다.",
                            "correct": false
                        }},
                        {{
                            "number": 5
                            "answer": "불법행위 책임은 계약의 당사자 사이에 국한된다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글에 대한 이해로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "자연 영상은 모델링과 렌더링 단계를 거치지 않고 생성된다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "렌더링에서 사용되는 물체 고유의 표면 특성은 화솟값에 의해 결정된다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "물체의 원근감과 입체감은 관찰 시점을 기준으로 구현한다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "3D 영상을 재현하는 화면의 해상도가 높을수록 연산 양이 많아진다.",
                            "correct": true
                        }},
                        {{
                            "number": 5
                            "answer": "병목 현상은 연산할 데이터의 양이 처리 능력을 초과할 때 발생한다.",
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

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

    문제의 유형은 주어진 텍스트에 등장하는 핵심 키워드 2개에 대한 내용을 이해했는지 질문하는 문제야.

    각 질문은 "'aaa'과 'bbb'를 이해한 내용으로 적절한 것은?"으로 통일해주고, 'aaa'는 첫 번째 핵심 키워드를 의미하고 'bbb'는 또 다른 핵심 키워드를 의미해.

    예시는 각각의 키워드에 대한 내용으로도 구성해주고, 두 키워드를 모두 사용한 내용도 구성해줘.

         
   Example Question
         
    Question: '예언화 경향'과 '전반적 대응설'을 이해한 내용으로 적절한 것은?
    Answer: 
    1. '예언화 경향'은 이전과 달리 인간사와 재이의 인과 관계를 역전시켜 재이를 인간사의 미래를 알려 주는 징조로 삼는 데 활용되었다.(o)
    2. '예언화 경향'은 군주의 과거 실정에 대한 경고로서 재이의 의미가 강조되어 신하의 직언을 활성화하는 방향으로 활용되었다.
    3. '전반적 대응설'은 개별적인 재이 현상을 물리적 작용이라 보고 정치와 무관하게 재이를 이해하는 기초로 활용되었다.
    4. '전반적 대응설'은 누적된 실정과 특정한 재이 현상을 연결 짓는 방식으로 이어져 군주의 권력을 강화하는 데 활용되었다.
    5. '전반적 대응설'은 과학적 인식을 기반으로 군주의 지배력과 변칙적인 자연 현상이 무관하다는 인식을 강화하는 기초로 활용되었다.
         
    Question: '전통적 인식론자'와 '베이즈주의자'에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 만약 을이 '전통적 인식론자'라면 을은 동시에 '베이즈주의자'일 수 없다.
    2. '전통적 인식론자'은 을이 "내일 눈이 온다."가 거짓이라 믿는 것은 그 명제가 거짓임을 강한 정도로 믿는다는 의미라고 주장한다.(o)
    3. '전통적 인식론자'는 을이 "내일 눈이 온다."가 참이라고 믿는다면 을은 ‘내일 눈이 온다.’가 거짓이라고 믿을 수는 없다고 주장한다.
    4. '베이즈주의자'는 을의 "내일 눈이 온다."가 참이라는 것에 대한 믿음의 정도와 ‘내일 눈이 온다.’가 거짓이라는 것에 대한 믿음의 정도가 같을 수 있다고 본다.
    5. '베이즈주의자'는 을이 "내일 눈이 온다."와 "내일 비가 온다."가 모두 거짓이라고 믿더라도 후자를 전자보다 더 강하게 거짓이라고 믿을 수 있다고 주장한다.
    
    Question: '생산학과'와 '소비학과'에 대한 이해로 적절한 것은?
    Answer: 
    1. '생산학과'는 근대 도시를 근대 도시인이 지닌 환상에 의해 작동되는 생산 기계라고 본다.
    2. '생산학과'는 새로운 테크놀로지의 발달로 성립된 근대 생산 체제가 욕망과 충족의 간극을 해소할 수 있다고 본다.
    3. '소비학과'는 근대 도시인의 소비 정신이 금욕주의 정신에 의해 만들어졌다고 본다.
    4. '소비학과'는 근대 도시인이 사물로 전락한 대상이 아니라 실현가능한 미래에 대한 기대를 가진 존재라고 본다.(o)
    5. '생산학과'와 '소비학과'는 모두 소비가 노동자에 대한 집단 규율을 완화하여 유순한 몸을 만든다고 본다.

         
    Question: '이중 가닥 DNA 특이 염료'과 '형광 표식 탐침'에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. '이중 가닥 DNA 특이 염료'는 '형광 표식 탐침'과 달리 프라이머와 결합하여 이합체를 이룬다.
    2. '이중 가닥 DNA 특이 염료'는 '형광 표식 탐침'과 달리 표적 DNA에 붙은 채 발색 반응이 일어난다.(o)
    3. '형광 표식 탐침'은 '이중 가닥 DNA 특이 염료과 달리 형광 물질과 결합하여 이합체를 이룬다.
    4. '형광 표식 탐침'은 '이중 가닥 DNA 특이 염료'과 달리 한 사이클의 시작 시점에 발색 반응이 일어난다.
    5. '이중 가닥 DNA 특이 염료'과 '형광 표식 탐침'은 모두 이중 가닥 표적 DNA에 결합하는 물질이다.

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

    Question: '예언화 경향'과 '전반적 대응설'을 이해한 내용으로 적절한 것은?
    Answer: 
    1. '예언화 경향'은 이전과 달리 인간사와 재이의 인과 관계를 역전시켜 재이를 인간사의 미래를 알려 주는 징조로 삼는 데 활용되었다.(o)
    2. '예언화 경향'은 군주의 과거 실정에 대한 경고로서 재이의 의미가 강조되어 신하의 직언을 활성화하는 방향으로 활용되었다.
    3. '전반적 대응설'은 개별적인 재이 현상을 물리적 작용이라 보고 정치와 무관하게 재이를 이해하는 기초로 활용되었다.
    4. '전반적 대응설'은 누적된 실정과 특정한 재이 현상을 연결 짓는 방식으로 이어져 군주의 권력을 강화하는 데 활용되었다.
    5. '전반적 대응설'은 과학적 인식을 기반으로 군주의 지배력과 변칙적인 자연 현상이 무관하다는 인식을 강화하는 기초로 활용되었다.
         
    Question: '전통적 인식론자'와 '베이즈주의자'에 대한 이해로 적절하지 않은 것은?
    Answer: 
    1. 만약 을이 '전통적 인식론자'라면 을은 동시에 '베이즈주의자'일 수 없다.
    2. '전통적 인식론자'은 을이 "내일 눈이 온다."가 거짓이라 믿는 것은 그 명제가 거짓임을 강한 정도로 믿는다는 의미라고 주장한다.(o)
    3. '전통적 인식론자'는 을이 "내일 눈이 온다."가 참이라고 믿는다면 을은 ‘내일 눈이 온다.’가 거짓이라고 믿을 수는 없다고 주장한다.
    4. '베이즈주의자'는 을의 "내일 눈이 온다."가 참이라는 것에 대한 믿음의 정도와 ‘내일 눈이 온다.’가 거짓이라는 것에 대한 믿음의 정도가 같을 수 있다고 본다.
    5. '베이즈주의자'는 을이 "내일 눈이 온다."와 "내일 비가 온다."가 모두 거짓이라고 믿더라도 후자를 전자보다 더 강하게 거짓이라고 믿을 수 있다고 주장한다.
    
    Question: '생산학과'와 '소비학과'에 대한 이해로 적절한 것은?
    Answer: 
    1. '생산학과'는 근대 도시를 근대 도시인이 지닌 환상에 의해 작동되는 생산 기계라고 본다.
    2. '생산학과'는 새로운 테크놀로지의 발달로 성립된 근대 생산 체제가 욕망과 충족의 간극을 해소할 수 있다고 본다.
    3. '소비학과'는 근대 도시인의 소비 정신이 금욕주의 정신에 의해 만들어졌다고 본다.
    4. '소비학과'는 근대 도시인이 사물로 전락한 대상이 아니라 실현가능한 미래에 대한 기대를 가진 존재라고 본다.(o)
    5. '생산학과'와 '소비학과'는 모두 소비가 노동자에 대한 집단 규율을 완화하여 유순한 몸을 만든다고 본다.


    Example Output:

    ```json
    {{ "questions": [
            {{
                "question": "'예언화 경향'과 '전반적 대응설'을 이해한 내용으로 적절한 것은?",
                "answers": [
                        {{
                            "number": 1,
                            "answer": "'예언화 경향'은 이전과 달리 인간사와 재이의 인과 관계를 역전시켜 재이를 인간사의 미래를 알려 주는 징조로 삼는 데 활용되었다.",
                            "correct": true
                        }},
                        {{
                            "number": 2,
                            "answer": "'예언화 경향'은 군주의 과거 실정에 대한 경고로서 재이의 의미가 강조되어 신하의 직언을 활성화하는 방향으로 활용되었다.",
                            "correct": false
                        }},
                        {{
                            "number": 3,
                            "answer": "'전반적 대응설'은 개별적인 재이 현상을 물리적 작용이라 보고 정치와 무관하게 재이를 이해하는 기초로 활용되었다.",
                            "correct": false
                        }},
                        {{
                            "number": 4,
                            "answer": "'전반적 대응설'은 누적된 실정과 특정한 재이 현상을 연결 짓는 방식으로 이어져 군주의 권력을 강화하는 데 활용되었다.",
                            "correct": false
                        }},
                        {{
                            "number": 5,
                            "answer": "'전반적 대응설'은 과학적 인식을 기반으로 군주의 지배력과 변칙적인 자연 현상이 무관하다는 인식을 강화하는 기초로 활용되었다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "'전통적 인식론자'와 '베이즈주의자'에 대한 이해로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "만약 을이 '전통적 인식론자'라면 을은 동시에 '베이즈주의자'일 수 없다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "'전통적 인식론자'은 을이 "내일 눈이 온다."가 거짓이라 믿는 것은 그 명제가 거짓임을 강한 정도로 믿는다는 의미라고 주장한다.",
                            "correct": true
                        }},
                        {{
                            "number": 3
                            "answer": "'전통적 인식론자'는 을이 "내일 눈이 온다."가 참이라고 믿는다면 을은 ‘내일 눈이 온다.’가 거짓이라고 믿을 수는 없다고 주장한다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "'베이즈주의자'는 을의 "내일 눈이 온다."가 참이라는 것에 대한 믿음의 정도와 ‘내일 눈이 온다.’가 거짓이라는 것에 대한 믿음의 정도가 같을 수 있다고 본다.",
                            "correct": false
                        }},
                        {{
                            "number": 5
                            "answer": "'베이즈주의자'는 을이 "내일 눈이 온다."와 "내일 비가 온다."가 모두 거짓이라고 믿더라도 후자를 전자보다 더 강하게 거짓이라고 믿을 수 있다고 주장한다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "'생산학과'와 '소비학과'에 대한 이해로 적절한 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "'생산학과'는 근대 도시를 근대 도시인이 지닌 환상에 의해 작동되는 생산 기계라고 본다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "'생산학과'는 새로운 테크놀로지의 발달로 성립된 근대 생산 체제가 욕망과 충족의 간극을 해소할 수 있다고 본다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "'소비학과'는 근대 도시인의 소비 정신이 금욕주의 정신에 의해 만들어졌다고 본다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "'소비학과'는 근대 도시인이 사물로 전락한 대상이 아니라 실현가능한 미래에 대한 기대를 가진 존재라고 본다.",
                            "correct": true
                        }},
                        {{
                            "number": 5
                            "answer": "'생산학과'와 '소비학과'는 모두 소비가 노동자에 대한 집단 규율을 완화하여 유순한 몸을 만든다고 본다.",
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

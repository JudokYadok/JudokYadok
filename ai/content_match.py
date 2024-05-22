# -*- coding: utf-8 -*-

import json
import sys
from langchain_community.document_loaders import TextLoader
from langchain_openai import ChatOpenAI
from langchain.prompts import ChatPromptTemplate, load_prompt
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
    openai_api_key = "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",
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


# 퀴즈 프롬프트
question_prompt_1 = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                """
    너는 교사 역할을 하는 유용한 보조자야.

    다음 문맥에 기초하여 3개의 질문을 만들어 주어진 텍스트에 대한 사용자의 지식을 테스트해줘.

    각 질문에는 5개의 예시 있어야 하며, 그 중 4개는 오답이고 1개는 정답이야.

    각 질문은 '윗글에 대한 설명으로 적절하지 않은 것은?'이나 '윗글에 대한 내용으로 적절한 것은?' 두 가지만 사용해줘.

    예시는 텍스트에 대한 구체적인 설명으로 정의해줘.

    문제의 유형은 텍스트에 대한 내용을 이해했는지에 대한 문제야.

    (o)를 사용해서 정답을 표시해줘.
         
    Example Question
         
    Question: 윗글에 대한 설명으로 적절하지 않은 것은?
    Answer: 
    1. 유물론적 인간관은 영혼의 존재를 인정하지 않는다.
    2. 유물론적 인간관은 인간의 선택을 물리적 사건으로 본다.
    3. 종교적 인간관은 인간이 물리적 실체로만 구성된다고 보지 않는다.
    4. 종교적 인간관은 인간의 선택에서 비물리적 실체가 하는 역할을 인정한다.(o)
    5. 반자유의지 논증은 임의의 선택이 선결정되지 않을 가능성을 고려하지 않는다.
         
    Question: 윗글의 내용과 일치하는 것은?
    Answer: 
    1. 차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.
    2. 외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.
    3. 차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.(o)
    4. 영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.
    5. 위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.
         
    Question: 윗글의 내용과 일치하지 않는 것은?
    Answer: 
    1. 감각 전달 장치와 공간 이동 장치는 사용자가 메타버스에 몰입할 수 있게 한다.
    2. 공간 이동 장치는 현실 세계 사용자의 움직임을 메타버스의 아바타에게 전달한다.
    3. HMD는 사용자가 시각을 통해 메타버스의 공간과 물체의 입체감을 느끼도록 한다.
    4. 감각 전달 장치는 아바타가 느끼는 것으로 설정된 감각을 사용자에게 전달하는 장치이다.
    5. 가상 현실 장갑을 착용하면 사용자와 아바타는 상호 간에 감각 반응을 주고받을 수 있다.(o)
         
         
    너의 차례야!
         
    Context: {context}
""",
            )
        ]
)


question_chain_1 = question_prompt_1 | llm




formatting_prompt_1 = ChatPromptTemplate.from_messages(
    [
        (
            "system",
            """
    너는 강력한 포맷 알고리즘이야.
    시험 문제의 형식을 JSON 형식으로 포맷해줘.
    JSON 형식은 key값을 큰 따옴표(")를 사용해야 하고, value의 true/false는 소문자로 작성해야 해.
    (o) 표시가 있는 1개의 보기만이 정답이야.

    Example Input:

     Question: 윗글에 대한 설명으로 적절하지 않은 것은?
    Answer: 
    1. 유물론적 인간관은 영혼의 존재를 인정하지 않는다.
    2. 유물론적 인간관은 인간의 선택을 물리적 사건으로 본다.
    3. 종교적 인간관은 인간이 물리적 실체로만 구성된다고 보지 않는다.
    4. 종교적 인간관은 인간의 선택에서 비물리적 실체가 하는 역할을 인정한다.(o)
    5. 반자유의지 논증은 임의의 선택이 선결정되지 않을 가능성을 고려하지 않는다.
         
    Question: 윗글의 내용과 일치하는 것은?
    Answer: 
    1. 차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.
    2. 외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.
    3. 차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.(o)
    4. 영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.
    5. 위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.
         
    Question: 윗글의 내용과 일치하지 않는 것은?
    Answer: 
    1. 감각 전달 장치와 공간 이동 장치는 사용자가 메타버스에 몰입할 수 있게 한다.
    2. 공간 이동 장치는 현실 세계 사용자의 움직임을 메타버스의 아바타에게 전달한다.
    3. HMD는 사용자가 시각을 통해 메타버스의 공간과 물체의 입체감을 느끼도록 한다.
    4. 감각 전달 장치는 아바타가 느끼는 것으로 설정된 감각을 사용자에게 전달하는 장치이다.
    5. 가상 현실 장갑을 착용하면 사용자와 아바타는 상호 간에 감각 반응을 주고받을 수 있다.(o)
         

    Example Output:

    ```json
    {{ "questions": [
            {{
                "question": "윗글에 대한 설명으로 적절하지 않은 것은?",
                "answers": [
                        {{
                            "number": 1,
                            "answer": "유물론적 인간관은 영혼의 존재를 인정하지 않는다.",
                            "correct": false
                        }},
                        {{
                            "number": 2,
                            "answer": "유물론적 인간관은 인간의 선택을 물리적 사건으로 본다.",
                            "correct": false
                        }},
                        {{
                            "number": 3,
                            "answer": "종교적 인간관은 인간이 물리적 실체로만 구성된다고 보지 않는다.",
                            "correct": false
                        }},
                        {{
                            "number": 4,
                            "answer": "종교적 인간관은 인간의 선택에서 비물리적 실체가 하는 역할을 인정한다.",
                            "correct": true
                        }},
                        {{
                            "number": 5,
                            "answer": "반자유의지 논증은 임의의 선택이 선결정되지 않을 가능성을 고려하지 않는다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글의 내용과 일치하는 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "차량 주위를 위에서 내려다본 것 같은 영상은 360도를 촬영 하는 카메라 하나를 이용하여 만들어진다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "외부 변수로 인한 왜곡은 카메라 자체의 특징을 알 수 있으면 쉽게 해결할 수 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "차량의 전후좌우 카메라에서 촬영된 영상을 하나의 영상으로 합성한 후 왜곡을 보정한다.",
                            "correct": true
                        }},
                        {{
                            "number": 4
                            "answer": "영상이 중심부로부터 멀수록 크게 휘는 것은 왜곡 모델을 설정하여 보정할 수 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 5
                            "answer": "위에서 내려다보는 시점의 영상에 있는 점들은 카메라 시점의 영상과는 달리 3차원 좌표로 표시된다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글의 내용과 일치하지 않는 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "감각 전달 장치와 공간 이동 장치는 사용자가 메타버스에 몰입할 수 있게 한다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "공간 이동 장치는 현실 세계 사용자의 움직임을 메타버스의 아바타에게 전달한다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "HMD는 사용자가 시각을 통해 메타버스의 공간과 물체의 입체감을 느끼도록 한다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "감각 전달 장치는 아바타가 느끼는 것으로 설정된 감각을 사용자에게 전달하는 장치이다.",
                            "correct": true
                        }},
                        {{
                            "number": 5
                            "answer": "가상 현실 장갑을 착용하면 사용자와 아바타는 상호 간에 감각 반응을 주고받을 수 있다.",
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

formatting_chain_1= formatting_prompt_1 | llm


# quiz chain과 format chain 합치기
chain = {"context": question_chain_1} | formatting_chain_1 | output_parser

result = chain.invoke({"context": chunk_list})

print(json.dumps(result, ensure_ascii=False, indent=2))
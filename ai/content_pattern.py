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


question_prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                """
    너는 교사 역할을 하는 유용한 보조자야.

    다음 문맥에 기초하여 3개의 질문을 만들어서 주어진 텍스트에 대한 사용자의 지식을 테스트해줘.

    각 질문에는 5개의 예시가 있어야 하며, 그 중 4개는 오답이고 1개는 정답이야.

    (o)를 사용해서 정답을 표시해줘.

    문제의 유형은 주어진 텍스트에 등장하는 내용의 전개 방식을 묻는 문제야.

    각 질문은 "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?" 이라고 통일해줘.


         
    Exmaple Question
         
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.
    2. 특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.(o)
    3. 특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.
    4. 특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.
    5. 특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.
         
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 역사의 개념을 밝히면서 영화와 역사 간의 공통점과 차이점을 비교하고 있다.
    2. 영화의 변천 과정을 통시적으로 밝혀 사료로서 영화가 지닌 의의를 강조하고 있다.
    3. 역사에 대한 서로 다른 견해를 대조하여 사료로서 영화가 지닌 한계를 비판하고 있다.(o)
    4. 영화의 사료로서의 특성을 밝히면서 역사 서술로서 영화가 지닌 가능성을 제시하고 있다.
    5. 다양한 영화의 유형별 장단점을 분석하여 영화가 역사 서술의 대안이 될 수 있는지에 대해 평가하고 있다.
         
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 개체성과 관련된 예를 제시한 후 공생발생설에 대한 다양한 견해를 비교하고 있다.
    2. 개체에 대한 정의를 제시한 후 세포의 생물학적 개념이 확립되는 과정을 서술하고 있다.
    3. 개체성의 조건을 제시한 후 세포 소기관의 개체성에 대해 공생발생설을 중심으로 설명하고 있다.
    4. 개체의 유형을 분류한 후 세포의 소기관이 분화되는 과정을 공생발생설을 중심으로 설명하고 있다.(o)
    5. 개체와 관련된 개념들을 설명한 후 세포가 하나의 개체로 변화하는 과정을 인과적으로 서술하고 있다.
         
         
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

   Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.
    2. 특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.(o)
    3. 특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.
    4. 특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.
    5. 특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.
         
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 역사의 개념을 밝히면서 영화와 역사 간의 공통점과 차이점을 비교하고 있다.
    2. 영화의 변천 과정을 통시적으로 밝혀 사료로서 영화가 지닌 의의를 강조하고 있다.
    3. 역사에 대한 서로 다른 견해를 대조하여 사료로서 영화가 지닌 한계를 비판하고 있다.(o)
    4. 영화의 사료로서의 특성을 밝히면서 역사 서술로서 영화가 지닌 가능성을 제시하고 있다.
    5. 다양한 영화의 유형별 장단점을 분석하여 영화가 역사 서술의 대안이 될 수 있는지에 대해 평가하고 있다.
         
    Question: 윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?
    Answer: 
    1. 개체성과 관련된 예를 제시한 후 공생발생설에 대한 다양한 견해를 비교하고 있다.
    2. 개체에 대한 정의를 제시한 후 세포의 생물학적 개념이 확립되는 과정을 서술하고 있다.
    3. 개체성의 조건을 제시한 후 세포 소기관의 개체성에 대해 공생발생설을 중심으로 설명하고 있다.
    4. 개체의 유형을 분류한 후 세포의 소기관이 분화되는 과정을 공생발생설을 중심으로 설명하고 있다.(o)
    5. 개체와 관련된 개념들을 설명한 후 세포가 하나의 개체로 변화하는 과정을 인과적으로 서술하고 있다.
         


    Example Output:

    ```json
    {{ "questions": [
            {{
                "question": "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?",
                "answers": [
                        {{
                            "number": 1,
                            "answer": "특정한 국제적 기준의 내용과 그 변화 양상을 서술하며 국제 사회에 작용하는 규범성을 설명하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 2,
                            "answer": "특정한 국제적 기준이 제정된 원인을 서술하며 국제 사회의 규범을 감독 권한의 발생 원인에 따라 분류하고 있다.",
                            "correct": true
                        }},
                        {{
                            "number": 3,
                            "answer": "특정한 국제적 기준의 필요성을 서술하며 국제 사회에 수용 되는 규범의 필요성을 상반된 관점에서 논증하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 4,
                            "answer": "특정한 국제적 기준과 관련된 국내법의 특징을 서술하며 국제 사회에 받아들여지는 규범의 장단점을 설명하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 5,
                            "answer": "특정한 국제적 기준의 설정 주체가 바뀐 사례를 서술하며 국제 사회에서 규범 설정 주체가 지닌 특징을 분석하고 있다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "역사의 개념을 밝히면서 영화와 역사 간의 공통점과 차이점을 비교하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "영화의 변천 과정을 통시적으로 밝혀 사료로서 영화가 지닌 의의를 강조하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "역사에 대한 서로 다른 견해를 대조하여 사료로서 영화가 지닌 한계를 비판하고 있다.",
                            "correct": true
                        }},
                        {{
                            "number": 4
                            "answer": "영화의 사료로서의 특성을 밝히면서 역사 서술로서 영화가 지닌 가능성을 제시하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 5
                            "answer": "다양한 영화의 유형별 장단점을 분석하여 영화가 역사 서술의 대안이 될 수 있는지에 대해 평가하고 있다.",
                            "correct": false
                        }},
                ]
            }},
                        {{
                "question": "윗글의 내용 전개 방식에 대한 설명으로 가장 적절한 것은?",
                "answers": [
                        {{
                            "number": 1
                            "answer": "개체성과 관련된 예를 제시한 후 공생발생설에 대한 다양한 견해를 비교하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 2
                            "answer": "개체에 대한 정의를 제시한 후 세포의 생물학적 개념이 확립되는 과정을 서술하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 3
                            "answer": "개체성의 조건을 제시한 후 세포 소기관의 개체성에 대해 공생발생설을 중심으로 설명하고 있다.",
                            "correct": false
                        }},
                        {{
                            "number": 4
                            "answer": "개체의 유형을 분류한 후 세포의 소기관이 분화되는 과정을 공생발생설을 중심으로 설명하고 있다.",
                            "correct": true
                        }},
                        {{
                            "number": 5
                            "answer": "개체와 관련된 개념들을 설명한 후 세포가 하나의 개체로 변화하는 과정을 인과적으로 서술하고 있다.",
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
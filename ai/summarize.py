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
    # callbacks=[StreamingStdOutCallbackHandler()],
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


summarize_prompt = ChatPromptTemplate.from_messages(
    [
        (
            "system",
            """
    너는 강력한 요약 도구야. 
    
    사용자가 텍스트를 이해할 수 있도록 주어진 긴 텍스트를 한 문단으로 간략히 요약해줘.

    다음의 예시를 참고하여 주어진 텍스트에 대한 요약을 해줘.
            
    첫 문장에는 '이 글은 독서에 초인지가 어떻게 동원되는지에 대해 설명하고 있다.', '이 글은 데이터에서 결측치와 이상치를 처리하는 방법에 대해 설명하고 있다.'라는 식으로 기술해줘.

    요약에 대한 내용만 출력해줘.

            

    요약 예시:

    이 글은 독서에 초인지가 어떻게 동원되는지에 대해 설명하고 있다. 독서에서의 초인지는 독자가 자신의 독서 행위에 대해 인지하는 것으로 자신의 독서 과정을 점검하고 조정하는 역할을 한다. 초인지는 글을 읽기 시작한 후 지속적으로 이루어지는 점검 과정에 동원된다. 이를 통해 독자는 독서 전략이 효과적이고 문제가 없는지를 평가하며 문제를 해결한다. 문제가 무엇인지 분명하지 않은 경우나 문제가 발생한 것을 독자 자신이 인지하지 못하는 경우에도 특정 방법들을 사용하여 문제 발생 여부를 점검할 수 있다. 또한 초인지는 문제를 해결하기 위해 독서 전략을 조정하는 과정에도 동원되는데, 독자는 이러한 초인지를 활용하여 점검과 조정을 되풀이하며 능동적으로 의미를 구성해 간다.

            # 이 글은 데이터에서 결측치와 이상치를 처리하는 방법에 대해 설명하고 있다. 결측치는 데이터 값이 빠져 있는 것으로, 결측치를 처리하는 방법 중 하나인 대체는 다른 값으로 결측치를 채우는 것이다. 이때 대체하는 값으로는 평균, 중앙값, 최빈값이 주로 사용된다. 한편 이상치는 데이터의 다른 값에 비해 유달리 크거나 작은 값으로, 데이터 수집 시 측정 오류에 의해 발생하기도 하고, 정상적인 데이터 중에도 존재할 수 있다. 평면상에 있는 점들의 위치를 나타내는 데이터에서도 이상치를 발견할 수 있는데, 이때 이상치를 처리하기 위해서는 데이터의 두 점을 지나는 가상의 직선과 각각의 데이터가 이루는 거리를 구하고, 이 거리가 허용 범위에 들어오는 정상치 집합의 데이터 개수가 최대인 직선을 찾는 기법을 사용한다.

            # 이 글은 데이터 소유권 주체에 관한 논의의 내용을 소개한 후, 최근에 논 의의 중심이 된 데이터 이동권에 관한 내용을 설명하고 있다. 데이터의 소유권이 누구에게 귀속되어야 하는지에 대한 논의에서는 데이터 소유권의 주체를 빅 데이터 보유자로 보는 견해와 정보 주체로 보는 견해가 있다. 최근에 우리나라는 데이터에 대해 소유권이 아닌 이동권을 법으로 명문화하여 정보 주체의 개인 정보 자기 결정권을 강화하였다. 그런데 이러한 데이터 이동권의 법제화로 데이터 생성 비용과 거래 비용을 줄일 수 있다는 견해가 있는 한편, 데이터가 특정 기업에 집중되어 데이터의 공유나 유통이 위축될 수 있다고 우려하는 견해도 있다.

    너의 차례야!

    Context: {context}
"""
        )
    ]
)

summarize_chain = summarize_prompt | llm

formatting_prompt = ChatPromptTemplate.from_messages(
    [
        (
            "system",
            """
    너는 강력한 포맷 알고리즘이야.
    시험 문제의 형식을 JSON 형식으로 포맷해줘.
    (o) 표시가 있는 1개의 보기만이 정답이야.

    Example Input:

    content : 이 글은 독서에 초인지가 어떻게 동원되는지에 대해 설명하고 있다. 독서에서의 초인지는 독자가 자신의 독서 행위에 대해 인지하는 것으로 자신의 독서 과정을 점검하고 조정하는 역할을 한다. 초인지는 글을 읽기 시작한 후 지속적으로 이루어지는 점검 과정에 동원된다. 이를 통해 독자는 독서 전략이 효과적이고 문제가 없는지를 평가하며 문제를 해결한다. 문제가 무엇인지 분명하지 않은 경우나 문제가 발생한 것을 독자 자신이 인지하지 못하는 경우에도 특정 방법들을 사용하여 문제 발생 여부를 점검할 수 있다. 또한 초인지는 문제를 해결하기 위해 독서 전략을 조정하는 과정에도 동원되는데, 독자는 이러한 초인지를 활용하여 점검과 조정을 되풀이하며 능동적으로 의미를 구성해 간다.


    Example Output:

    ```json
    {{
    "content": "이 글은 독서에 초인지가 어떻게 동원되는지에 대해 설명하고 있다. 독서에서의 초인지는 독자가 자신의 독서 행위에 대해 인지하는 것으로, 자신의 독서 과정을 점검하고 조정하는 역할을 한다. 초인지는 글을 읽기 시작한 후 지속적으로 이루어지는 점검 과정에 동원된다. 이를 통해 독자는 독서 전략이 효과적이고 문제가 없는지를 평가하며 문제를 해결한다. 문제가 무엇인지 분명하지 않은 경우나 문제가 발생한 것을 독자 자신이 인지하지 못하는 경우에도 특정 방법들을 사용하여 문제 발생 여부를 점검할 수 있다. 또한 초인지는 문제를 해결하기 위해 독서 전략을 조정하는 과정에도 동원되는데, 독자는 이러한 초인지를 활용하여 점검과 조정을 되풀이하며 능동적으로 의미를 구성해 간다."
    }}
    ```

    요약에 대한 내용만 출력해줘.
    
    너의 차례야!

    Context: {context}

""",

        )
    ]
)

formatting_chain = formatting_prompt | llm


# quiz chain과 format chain 합치기
chain = {"context": summarize_chain} | formatting_chain | output_parser

result = chain.invoke({"context": chunk_list})
print(json.dumps(result, ensure_ascii=False, indent=2))

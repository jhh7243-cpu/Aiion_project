import { NextRequest, NextResponse } from "next/server";
import { getServiceFromEureka } from "@/lib/eureka-service";

export async function GET(request: NextRequest)
{
  try
  {
    const searchParams = request.nextUrl.searchParams;
    const keyword = searchParams.get("keyword");
    const type = searchParams.get("type");

    // 백엔드와 동일하게 keyword와 type 모두 선택적
    // 하지만 keyword가 없으면 검색할 수 없으므로 keyword만 필수로 체크
    if (!keyword || !keyword.trim())
    {
      return NextResponse.json(
        { Code: 400, message: "⚠️ 검색어를 입력해주세요.", data: null },
        { status: 400 }
      );
    }

    // Step 1: Eureka를 통해 Discovery Gateway 서비스 조회
    console.log("[Step 1] Eureka에서 Discovery Gateway 조회 중...");
    const gatewayInstance = await getServiceFromEureka('gateway-server');
    
    if (!gatewayInstance) {
      return NextResponse.json(
        { Code: 503, message: "❌ Eureka에서 Discovery Gateway를 찾을 수 없습니다. 서비스가 등록되었는지 확인해주세요.", data: null },
        { status: 503 }
      );
    }

    // Step 2: Discovery Gateway를 통해 Soccer Service로 라우팅
    const gatewayUrl = `http://${gatewayInstance.hostName}:${gatewayInstance.port.$}`;
    const apiGatewayUrl = process.env.NEXT_PUBLIC_API_GATEWAY_URL || gatewayUrl;
    let url = `${apiGatewayUrl}/soccer/findByWord?keyword=${encodeURIComponent(keyword)}`;
    if (type && type.trim())
    {
      url += `&type=${encodeURIComponent(type)}`;
    }
    
    console.log("[Step 2] Discovery Gateway를 통해 요청 전송:", url);
    console.log("[Step 3] Soccer Service로 요청 라우팅 중...");

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Origin": "http://localhost:3000",
        "Referer": "http://localhost:3000"
      },
    });

    if (!response.ok)
    {
      let errorData;
      try
      {
        errorData = await response.json();
      }
      catch
      {
        const errorText = await response.text();
        errorData = { message: errorText || "서버 오류가 발생했습니다." };
      }
      console.error("Backend GET error:", response.status, errorData);
      return NextResponse.json(
        { Code: response.status, message: errorData.message || errorData.error || "서버 오류가 발생했습니다.", data: null },
        { status: response.status }
      );
    }

    const data = await response.json();
    console.log("[Step 4] Soccer Service 응답 수신 성공");
    return NextResponse.json(data);
  }
  catch (error)
  {
    console.error("API route GET error:", error);
    const errorMessage = error instanceof Error ? error.message : String(error);
    console.error("Error details:", {
      message: errorMessage,
      stack: error instanceof Error ? error.stack : undefined
    });
    
    // 연결 오류인지 확인
    if (errorMessage.includes("ECONNREFUSED") || errorMessage.includes("fetch failed") || errorMessage.includes("connect"))
    {
      const apiGatewayUrl = process.env.NEXT_PUBLIC_API_GATEWAY_URL || 'http://localhost:8080';
      return NextResponse.json(
        { Code: 500, message: `❌ API Gateway(${apiGatewayUrl})에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.`, data: null },
        { status: 500 }
      );
    }
    
    return NextResponse.json(
      { Code: 500, message: `서버 오류: ${errorMessage}`, data: null },
      { status: 500 }
    );
  }
}

export async function POST(request: NextRequest)
{
  try
  {
    const body = await request.json();
    const { type, keyword } = body;

    // 검색어는 필수
    if (!keyword || !keyword.trim())
    {
      return NextResponse.json(
        { Code: 400, message: "⚠️ 검색어를 입력해주세요.", data: null },
        { status: 400 }
      );
    }

    // 백엔드는 type이 null이어도 처리하지만, 프론트엔드에서는 명확성을 위해 type을 요구
    // 백엔드 코드를 보면 request가 null이어도 처리하므로, type은 선택적으로 보낼 수 있음
    const requestBody: { keyword: string; type?: string } = {
      keyword: keyword.trim(),
    };
    
    if (type && type.trim())
    {
      requestBody.type = type.trim();
    }

    // Step 1: Eureka를 통해 Discovery Gateway 서비스 조회
    console.log("[Step 1] Eureka에서 Discovery Gateway 조회 중...");
    const gatewayInstance = await getServiceFromEureka('gateway-server');
    
    if (!gatewayInstance) {
      return NextResponse.json(
        { Code: 503, message: "❌ Eureka에서 Discovery Gateway를 찾을 수 없습니다. 서비스가 등록되었는지 확인해주세요.", data: null },
        { status: 503 }
      );
    }

    // Step 2: Discovery Gateway를 통해 Soccer Service로 라우팅
    const gatewayUrl = `http://${gatewayInstance.hostName}:${gatewayInstance.port.$}`;
    const apiGatewayUrl = process.env.NEXT_PUBLIC_API_GATEWAY_URL || gatewayUrl;
    const url = `${apiGatewayUrl}/soccer/findByWord`;
    
    console.log("[Step 2] Discovery Gateway를 통해 요청 전송:", url, requestBody);
    console.log("[Step 3] Soccer Service로 요청 라우팅 중...");

    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Origin": "http://localhost:3000",
        "Referer": "http://localhost:3000"
      },
      body: JSON.stringify(requestBody),
    });

    if (!response.ok)
    {
      let errorData;
      try
      {
        errorData = await response.json();
      }
      catch
      {
        const errorText = await response.text();
        errorData = { message: errorText || "서버 오류가 발생했습니다." };
      }
      console.error("Backend POST error:", response.status, errorData);
      return NextResponse.json(
        { Code: response.status, message: errorData.message || errorData.error || "서버 오류가 발생했습니다.", data: null },
        { status: response.status }
      );
    }

    const data = await response.json();
    console.log("[Step 4] Soccer Service 응답 수신 성공");
    return NextResponse.json(data);
  }
  catch (error)
  {
    console.error("API route POST error:", error);
    const errorMessage = error instanceof Error ? error.message : String(error);
    console.error("Error details:", {
      message: errorMessage,
      stack: error instanceof Error ? error.stack : undefined
    });
    
    // 연결 오류인지 확인
    if (errorMessage.includes("ECONNREFUSED") || errorMessage.includes("fetch failed") || errorMessage.includes("connect"))
    {
      const apiGatewayUrl = process.env.NEXT_PUBLIC_API_GATEWAY_URL || 'http://localhost:8080';
      return NextResponse.json(
        { Code: 500, message: `❌ API Gateway(${apiGatewayUrl})에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.`, data: null },
        { status: 500 }
      );
    }
    
    return NextResponse.json(
      { Code: 500, message: `서버 오류: ${errorMessage}`, data: null },
      { status: 500 }
    );
  }
}


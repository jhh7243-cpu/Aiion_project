"use client";

import { useState } from "react";
import axios from "axios";

// SearchRequestDTO의 type 필드에 맞는 검색 타입
// 가능한 값: player, team, schedule, stadium
type SearchType = "player" | "team" | "schedule" | "stadium" | "";

interface SearchResult
{
  Code: number;
  message: string;
  data: any;
}

export default function Home()
{
  const [inputText, setInputText] = useState("");
  const [searchType, setSearchType] = useState<SearchType>("");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState<any>(null);
  const [error, setError] = useState<string | null>(null);

  const sendMessage = async () =>
  {
    // 검색어 검증
    if (!inputText.trim())
    {
      alert("⚠️ 검색어를 입력해주세요.");
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try
    {
      // Next.js API Route를 통해 API Gateway로 요청 전송
      const apiRouteUrl = "/api/soccer/findByWord";
      
      const params: { keyword: string; type?: string } = {
        keyword: inputText.trim(),
      };
      
      if (searchType && searchType.trim())
      {
        params.type = searchType.trim();
      }
      
      console.log("API Route로 요청 전송:", apiRouteUrl, params);
      const response = await axios.get(apiRouteUrl, {
        params,
        headers: {
          "Content-Type": "application/json",
        },
      });

      console.log("서버 응답:", response.data);

      const responseData: SearchResult = response.data;

      if (responseData)
      {
        const { Code, message, data } = responseData;

        // 알럿 메시지 표시 (검색어 포함)
        if (message)
        {
          const searchInfo = `검색어: "${inputText.trim()}"\n${message}`;
          alert(searchInfo);
          console.log("알럿 메시지:", searchInfo);
        }

        if (Code === 200)
        {
          console.log("✅ 검색 성공:", data);
          setResult(data);
        }
        else
        {
          console.error("❌ 검색 실패:", message);
          setError(message);
        }
      }
    }
    catch (error)
    {
      console.error("에러 발생:", error);

      if (axios.isAxiosError(error))
      {
        if (error.response)
        {
          // 서버가 응답했지만 에러 상태 코드
          const responseData = error.response.data || {};
          const { Code, message } = responseData;
          const errorMessage = message || responseData.error || `❌ 검색 중 오류가 발생했습니다. (상태 코드: ${error.response.status})`;
          console.error("서버 응답 오류:", {
            status: error.response.status,
            statusText: error.response.statusText,
            data: responseData
          });
          alert(errorMessage);
          setError(errorMessage);
        }
        else if (error.request)
        {
          // 요청은 보냈지만 응답을 받지 못함
          const errorMessage = "❌ API Gateway에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.";
          console.error("요청 전송 실패:", error.request);
          alert(errorMessage);
          setError(errorMessage);
        }
        else
        {
          // 요청 설정 중 에러
          const errorMessage = `❌ 요청 중 오류가 발생했습니다: ${error.message}`;
          console.error("요청 설정 오류:", error.message);
          alert(errorMessage);
          setError(errorMessage);
        }
      }
      else
      {
        const errorMessage = error instanceof Error ? error.message : "알 수 없는 오류";
        alert(`❌ 오류: ${errorMessage}`);
        setError(errorMessage);
      }
    }
    finally
    {
      setLoading(false);
    }
  };

  const handleSearch = () =>
  {
    sendMessage();
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-to-b from-slate-900 via-black to-black font-sans">
      <main className="flex min-h-screen w-full max-w-4xl flex-col items-center justify-center px-8 py-16">
        {/* Greeting Message */}
        <div className="flex flex-col items-center gap-4 text-center mb-12">
          <h1 className="text-4xl font-semibold leading-tight text-white mb-2">
            한국 축구 k리그 관련 질문만 받습니다.
          </h1>
        </div>

        {/* Search Type Selector */}
        <div className="w-full max-w-2xl mb-4">
          <label className="block text-sm text-gray-300 mb-2">
            검색 타입: <span className="text-gray-400">(선택사항)</span>
          </label>
          <select
            value={searchType}
            onChange={(e) => setSearchType(e.target.value as SearchType)}
            disabled={loading}
            className="w-full px-4 py-2 bg-white rounded-xl text-gray-700 border border-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <option value="">검색 타입 선택 (선택사항)</option>
            <option value="player">선수 (player)</option>
            <option value="team">팀 (team)</option>
            <option value="schedule">일정 (schedule)</option>
            <option value="stadium">경기장 (stadium)</option>
          </select>
        </div>

        {/* Input Field */}
        <div className="w-full max-w-2xl mb-8">
          <div className="relative flex items-center bg-white rounded-2xl px-6 py-4 shadow-lg">
            <input
              type="text"
              value={inputText}
              onChange={(e) => setInputText(e.target.value)}
              onKeyDown={(e) =>
              {
                if (e.key === "Enter" && !loading)
                {
                  sendMessage();
                }
              }}
              disabled={loading}
              placeholder="검색어를 입력하세요 (예: 토트넘, 서울 등)"
              className="flex-1 px-2 py-2 text-gray-700 placeholder-gray-400 outline-none bg-transparent disabled:opacity-50"
            />
            <div className="flex items-center gap-3">
              <button 
                onClick={handleSearch}
                disabled={loading}
                className="p-2 text-gray-400 hover:text-gray-600 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? (
                  <svg className="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                ) : (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                  </svg>
                )}
              </button>
            </div>
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <div className="w-full max-w-2xl mb-4 p-4 bg-red-900/50 border border-red-700 rounded-xl text-red-200">
            <strong>에러:</strong> {error}
          </div>
        )}

        {/* Search Results */}
        {result && (
          <div className="w-full max-w-4xl mt-6 p-6 bg-gray-800/50 border border-gray-700 rounded-xl">
            <h3 className="text-xl font-semibold text-white mb-4">
              검색 결과 ({Array.isArray(result) ? result.length : 0}개):
            </h3>
            <div className="bg-black/50 rounded-lg p-4 overflow-auto max-h-96">
              <pre className="text-sm text-gray-300 whitespace-pre-wrap">
                {JSON.stringify(result, null, 2)}
              </pre>
            </div>
          </div>
        )}

        {/* Usage Examples */}
        <div className="w-full max-w-2xl mt-8 p-4 bg-blue-900/30 border border-blue-700 rounded-xl">
          <h4 className="text-lg font-semibold text-blue-200 mb-3">💡 사용 예시:</h4>
          <ul className="text-sm text-gray-300 space-y-1">
            <li><strong>Player:</strong> 선수명, 포지션, 국가</li>
            <li><strong>Team:</strong> 토트넘, Tottenham, 서울</li>
            <li><strong>Schedule:</strong> 2024, 경기일정</li>
            <li><strong>Stadium:</strong> 서울월드컵경기장, 잠실</li>
          </ul>
        </div>
      </main>
    </div>
  );
}


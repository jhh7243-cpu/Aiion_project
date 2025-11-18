/**
 * Eureka Service Discovery 유틸리티
 * Eureka를 통해 서비스 정보를 조회합니다.
 */

export interface EurekaServiceInstance {
  instanceId: string;
  hostName: string;
  app: string;
  ipAddr: string;
  status: string;
  port: {
    $: number;
    '@enabled': string;
  };
  securePort: {
    $: number;
    '@enabled': string;
  };
  homePageUrl: string;
  statusPageUrl: string;
  healthCheckUrl: string;
  vipAddress: string;
  secureVipAddress: string;
}

export interface EurekaApplication {
  name: string;
  instance: EurekaServiceInstance[];
}

export interface EurekaApplications {
  applications: {
    application: EurekaApplication[];
  };
}

/**
 * Eureka에서 서비스 정보 조회
 * Eureka는 서비스 이름을 대문자로 저장하므로 대문자로 변환하여 조회
 */
export async function getServiceFromEureka(serviceName: string): Promise<EurekaServiceInstance | null> {
  try {
    const eurekaUrl = process.env.NEXT_PUBLIC_EUREKA_URL || 'http://localhost:8761';
    // Eureka는 서비스 이름을 대문자로 저장
    const upperServiceName = serviceName.toUpperCase();
    const url = `${eurekaUrl}/eureka/apps/${upperServiceName}`;
    
    console.log(`[Eureka] 서비스 조회: ${url}`);
    
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      // 대문자로 실패하면 원본 이름으로 재시도
      if (serviceName !== upperServiceName) {
        const fallbackUrl = `${eurekaUrl}/eureka/apps/${serviceName}`;
        console.log(`[Eureka] 대문자 조회 실패, 원본 이름으로 재시도: ${fallbackUrl}`);
        const fallbackResponse = await fetch(fallbackUrl, {
          method: 'GET',
          headers: {
            'Accept': 'application/json',
          },
        });
        
        if (!fallbackResponse.ok) {
          console.error(`[Eureka] 서비스 조회 실패: ${fallbackResponse.status}`);
          return null;
        }
        
        const fallbackData = await fallbackResponse.json();
        const application = fallbackData.application;
        
        if (!application || !application.instance || application.instance.length === 0) {
          console.warn(`[Eureka] 서비스 인스턴스를 찾을 수 없음: ${serviceName}`);
          return null;
        }

        const instance = Array.isArray(application.instance) 
          ? application.instance.find((inst: EurekaServiceInstance) => inst.status === 'UP') || application.instance[0]
          : application.instance;

        console.log(`[Eureka] 서비스 발견: ${serviceName}`, instance);
        return instance;
      }
      
      console.error(`[Eureka] 서비스 조회 실패: ${response.status}`);
      return null;
    }

    const data = await response.json();
    const application = data.application;
    
    if (!application || !application.instance || application.instance.length === 0) {
      console.warn(`[Eureka] 서비스 인스턴스를 찾을 수 없음: ${serviceName}`);
      return null;
    }

    // 첫 번째 활성 인스턴스 반환
    const instance = Array.isArray(application.instance) 
      ? application.instance.find((inst: EurekaServiceInstance) => inst.status === 'UP') || application.instance[0]
      : application.instance;

    console.log(`[Eureka] 서비스 발견: ${serviceName}`, instance);
    return instance;
  } catch (error) {
    console.error(`[Eureka] 서비스 조회 중 오류:`, error);
    return null;
  }
}

/**
 * Eureka에서 모든 서비스 목록 조회
 */
export async function getAllServicesFromEureka(): Promise<EurekaApplication[]> {
  try {
    const eurekaUrl = process.env.NEXT_PUBLIC_EUREKA_URL || 'http://localhost:8761';
    const url = `${eurekaUrl}/eureka/apps`;
    
    console.log(`[Eureka] 모든 서비스 조회: ${url}`);
    
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    });

    if (!response.ok) {
      console.error(`[Eureka] 서비스 목록 조회 실패: ${response.status}`);
      return [];
    }

    const data: EurekaApplications = await response.json();
    const applications = data.applications?.application || [];
    
    console.log(`[Eureka] 등록된 서비스 수: ${applications.length}`);
    return applications;
  } catch (error) {
    console.error(`[Eureka] 서비스 목록 조회 중 오류:`, error);
    return [];
  }
}


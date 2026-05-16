export type LogType = 'request' | 'response' | 'error';

export interface LogEntry {
  type: LogType;
  message: string;

  apiUrl: string;
  method: 'GET' | 'POST' | 'PUT' | 'DELETE';

  payload?: any;           // request or response body
  status?: number;         // response status code

  timestamp: string;
}

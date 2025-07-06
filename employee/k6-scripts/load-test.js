import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 20,
  duration: '1m',
};

export default function () {
  let res = http.get('http://springboot-app:8080/api/employees');
  check(res, {
    'status is 200': (r) => r.status === 200,
  });
  sleep(1);
}

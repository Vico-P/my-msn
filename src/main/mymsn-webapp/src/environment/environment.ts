export const environment = {
  production: false,
  baseUrl: 'http://localhost:4200',
  // In proxy.conf.json we set a new target to 8080 port use in dev mode for backend server to avoid CORS problem
  apiUrl: 'http://localhost:4200/api',
};

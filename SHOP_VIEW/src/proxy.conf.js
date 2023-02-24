const PROXY_CONFIG = [
  {
    context: [
      "/datnapi/api",
    ],
    target: "http://localhost:8081",
    secure: false
  },
]
module.exports = PROXY_CONFIG;

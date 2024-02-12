FROM node:20

COPY package.json package.json
RUN npm install

COPY public public
COPY src src
COPY .env .env

CMD ["npm", "start"]

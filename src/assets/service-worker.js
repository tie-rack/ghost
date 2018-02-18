const CACHE = 'ghost-cache';
const ASSETS = ['/', '/index.html', '/js/ghost.js', '/css/site.css'];

const precache = () =>
  caches.open(CACHE).then((cache) =>
    cache.addAll(ASSETS));

const fromCache = (request) =>
  caches.open(CACHE).then((cache) =>
    cache.match(request).then((matching) =>
      matching || Promise.reject('no-match')));

const update = (request) =>
  caches.open(CACHE).then((cache) =>
    fetch(request).then((response) =>
      cache.put(request, response)));

self.addEventListener('install', (e) => {
  console.log('Installing ghost');
  e.waitUntil(precache());
});

self.addEventListener('fetch', (e) => {
  console.log('Serving from cache');
  e.respondWith(fromCache(e.request));
  e.waitUntil(update(e.request));
});

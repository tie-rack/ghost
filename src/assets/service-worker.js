const CACHE = 'ghost-cache';
const ASSETS = ['/',
                '/index.html',
                '/js/ghost.js',
                '/css/site.css',
                '/manifest.webmanifest'];

self.addEventListener('install', function(evt) {
  console.log('Installing Ghost...');
  evt.waitUntil(precache());
});

self.addEventListener('fetch', function(evt) {
  console.log('Serving from Ghost cache.');
  evt.respondWith(fromCache(evt.request));
  evt.waitUntil(update(evt.request));
});

function precache() {
  return caches.open(CACHE).then(function (cache) {
    return cache.addAll(ASSETS);
  });
}

function fromCache(request) {
  return caches.open(CACHE).then(function (cache) {
    return cache.match(request).then(function (matching) {
      return matching || Promise.reject('no-match');
    });
  });
}

function update(request) {
  return caches.open(CACHE).then(function (cache) {
    return fetch(request).then(function (response) {
      return cache.put(request, response);
    });
  });
}

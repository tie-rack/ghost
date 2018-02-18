// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

const ghost = (() => {
  let username, media;

  const app = document.getElementById('app');

  const createElement = (tagName, options, ...children) => {
    let element = document.createElement(tagName);
    if (options.id) { element.id = options.id; }
    if (options.classes) { element.classList.add(... options.classes); }
    if (options.text) { element.textContent = options.text; }
    if (options.attrs) {
      Object.getOwnPropertyNames(options.attrs).forEach((attr) => {
        element.setAttribute(attr, options.attrs[attr]);
      });
    }
    if (options.listeners) {
      Object.getOwnPropertyNames(options.listeners).forEach((listener) => {
        element.addEventListener(listener, options.listeners[listener]);
      });
    }
    if (children) {
      element.append(...children);
    }
    return element;
  };

  const upload = () => document.getElementById('upload').click();

  const addPostFromForm = () => {
    let textArea = document.getElementById('content');
    let postContent = textArea.value;
    let postMedia = media;
    if (postMedia) {
      document.getElementById('add-picture').classList.remove('hidden');
      document.getElementById('remove-picture').classList.add('hidden');
    };
    textArea.value = '';
    media = null;

    addPost(postContent, postMedia);
  }

  const addPost = (postContent, postMedia) => {
    if (postContent || postMedia) {
      let authorDiv = createElement('div',
                                    { classes: ['author'],
                                      text: `@${username}` });
      let postContentDiv = createElement('div',
                                         { classes: ['post-content'] });
      if (postContent) {
        let contentDiv = createElement('div',
                                       { classes: ['content'],
                                         text: postContent });
        postContentDiv.append(contentDiv);
      }
      if (postMedia) {
        let img = createElement('img',
                                { classes: ['media', 'width-auto'],
                                  attrs: { src: postMedia }});
        postContentDiv.append(img);
      }
      let postBody = createElement('div', {}, authorDiv, postContentDiv);

      let repostButton = createElement('button',
                                       { classes: ['btn'],
                                         text: 'repost',
                                         listeners: { click: () => {
                                           addPost(postContent, postMedia);
                                           window.scrollTo(0,0);
                                         }}});
      let deleteButton = createElement('button',
                                       { classes: ['btn'],
                                         text: 'delete' });
      let buttons = createElement('div',
                                  { classes: ['post-buttons'] },
                                  repostButton, deleteButton);
      let post = createElement('div',
                               { classes: ['post'] },
                               postBody, buttons);

      let posts = document.getElementById('posts');

      deleteButton.addEventListener('click', () => posts.removeChild(post));

      posts.prepend(post);
    }
  };

  const setMedia = (e) => {
    let file = e.target.files[0];
    let fileReader = new FileReader();
    fileReader.onload = (e) => {
      media = e.target.result;
      document.getElementById('add-picture').classList.add('hidden');
      document.getElementById('remove-picture').classList.remove('hidden');
    };
    fileReader.readAsDataURL(file);
  };

  const removeUpload = () => {
    media = null;
    document.getElementById('add-picture').classList.remove('hidden');
    document.getElementById('remove-picture').classList.add('hidden');
  };

  const login = () => {
    username = document.getElementById('username').value;
    while (app.firstChild) {
      app.removeChild(app.firstChild);
    }

    let logout = createElement('button',
                               { classes: ['btn-link'],
                                 text: 'Log out',
                                 listeners: { click: () => window.location.reload() }});
    let header = createElement('div', {}, `@${username}`, logout);

    let textArea = createElement('textArea',
                                 { classes: ['textarea'],
                                   id: 'content' });

    let pictureButton = createElement('button',
                                      { classes: ['btn'],
                                        id: 'add-picture',
                                        text: 'Add picture',
                                        listeners: { click: upload }});
    let removePicture = createElement('button',
                                      { classes: ['btn-link', 'hidden'],
                                        id: 'remove-picture',
                                        text: 'Remove picture',
                                        listeners: { click: removeUpload }});

    let uploadInput = createElement('input',
                                    { id: 'upload',
                                      classes: ['hidden'],
                                      attrs: { accept: 'image/*',
                                               type: 'file' },
                                      listeners: { change: setMedia }});

    let postButton = createElement('button',
                                   { classes: ['btn'],
                                     text: 'Post',
                                     listeners: { click: addPostFromForm }});

    let composeButtons = createElement('div',
                                       { classes: ['compose-buttons'] },
                                       pictureButton,
                                       removePicture,
                                       uploadInput,
                                       postButton);

    let composeDiv = createElement('div',
                                   { classes: ['compose'] },
                                   textArea, composeButtons);

    let posts = createElement('div',
                              { classes: ['posts'],
                                id: 'posts' });

    app.append(header, composeDiv, posts);
  }

  return {
    login: login,
  }
})();

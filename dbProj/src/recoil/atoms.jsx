import { atom } from 'recoil';

export const userState = atom({
  key: 'userState',
  default: JSON.parse(localStorage.getItem('user'))||null,
});


export const isAuthenticatedState = atom({
  key: 'isAuthenticatedState',
  default: !!localStorage.getItem('token'),
});

export const notificationsState = atom({
  key: 'notificationsState',
  default: [], // Default is an empty array to store notifications.
});

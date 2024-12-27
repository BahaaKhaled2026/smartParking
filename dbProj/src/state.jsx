import { atom } from 'recoil';

export const currPanel = atom({
  key: 'currPanel',
  default: 1,
});
export const chosenLot = atom({
    key: 'chosenLot',
    default: null,
});
export const currUser = atom({
    key: 'currUser',
    default: JSON.parse(localStorage.getItem('user')),
});



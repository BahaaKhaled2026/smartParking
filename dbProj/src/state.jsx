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
export const changedReservations = atom({
  key: 'changedReservations',
  default: false,
});

export const changedLots = atom({
  key: 'changedLots',
  default: false,
});
export const notifi = atom({
  key: 'notifi',
  default: false,
});




## ERD

```
Table concert_seat {
  seat_id bigint [pk, increment]
  concert_id bigint [not null, ref: > concert.concert_id]
  seat_no int [not null, note: '1..50']
  seat_status varchar [not null, note: 'AVAILABLE|HELD|CONFIRMED']
  occupied_by_user_uuid uuid [ref: > users.user_uuid]

  Indexes {
    (seat_no) [unique]
  }

}



Table users {
 user_uuid uuid [pk]
 created_at timestamp [not null] 
}

Table queue_token {
  token_id uuid [pk]
  user_uuid uuid [not null, ref: > users.user_uuid]
  token_status varchar [not null, note: 'WAITING|ACTIVE|EXPIRED']
  created_at timestamp [not null]
  expires_at timestamp
}

Table concert {
  concert_id bitint [pk, increment]
  concert_date date [not null, unique]
  created_at timestamp
}

Table payment {
  payment_id bigint [pk, increment]
  user_uuid uuid [not null, ref: > users.user_uuid]
  seat_id bigint [not null, ref: > concert_seat.seat_id]
  amount bigint [not null]
  created_at timestamp [not null]
}




```
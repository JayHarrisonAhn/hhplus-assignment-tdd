import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    stages: [
        { duration: '10s', target: 30 },
        { duration: '5s', target: 600 },
        { duration: '20s', target: 30 },
        { duration: '5s', target: 600 },
        { duration: '10s', target: 0 },
    ],
    thresholds: {
        "http_req_duration" : [{threshold:"p(95)<2000", aboutOnFail: true}]
    }
};

export default function () {
    const url = `http://host.docker.internal:8080`

    // Create User
    const userRes = http.post(
        `${url}/user`,
        JSON.stringify({
            username: `user_${__VU}_${__ITER}`
        }),
        {headers:{'Content-Type':'application/json'}}
    );
    check(userRes, {
        "verify status": r=> r.status === 200
    })
    sleep(1)

    const userId = userRes.json().user.id

    // Create Token
    const tokenRes = http.post(
        `${url}/token`,
        JSON.stringify({}),
        {headers:{'Authorization':userId, 'Content-Type':'application/json'}}
    )
    check(tokenRes, {
        "verify status": r=> r.status === 200
    })
    sleep(1)

    // Check Token Activeness
    const token = tokenRes.json().token.id
    while(true) {
        const tokenCheckRes = http.get(
            `${url}/token`,
            {headers:{'Authorization':userId, 'Token':token, 'Content-Type':'application/json'}}
        )
        check(tokenCheckRes, {
            "verify status": r=> r.status === 200
        })
        if (tokenCheckRes.json().token.status === "ACTIVE") {
            break
        } else if (tokenCheckRes.json().token.status === "EXPIRE") {
            return
        }
        sleep(1)
    }

    // Get Concert Timeslot
    const timeslotRes = http.get(
        `${url}/concert/1/timeSlot`,
        {headers:{'Authorization':userId, 'Token':token, 'Content-Type':'application/json'}}
    )
    check(timeslotRes, {
        "verify status": r=> r.status === 200
    })
    sleep(1)

    const timeslotId = timeslotRes.json().timeSlots[0].id

    // Get Concert Seat
    const seatRes = http.get(
        `${url}/concert/1/timeSlot/${timeslotId}/seat`,
        {headers:{'Authorization':userId, 'Token':token, 'Content-Type':'application/json'}}
    )
    check(seatRes, {
        "verify status": r=> r.status === 200
    })
    sleep(1)

    if (!seatRes.json().seats[0].isEmpty) {
        return
    }

    const seatId = seatRes.json().seats[0].id

    // Occupy Concert Seat
    const occupyRes = http.post(
        `${url}/concert/1/timeSlot/${timeslotId}/seat/${seatId}`,
        JSON.stringify({
            userId: userId
        }),
        {headers:{'Authorization':userId, 'Token':token, 'Content-Type':'application/json'}}
    )
    sleep(1)
}
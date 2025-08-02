# PlaceLive
<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://github.com/user-attachments/assets/aad14b50-0f25-4d48-85e8-725505a09e03" width="200"/>
  <img src="https://github.com/user-attachments/assets/fb0334bf-f921-43a4-b889-61a7a76247f0" width="200"/>
  <img src="https://github.com/user-attachments/assets/662004a7-6e5d-4fb5-b930-7baa6bf70130" width="200"/>
  <img src="https://github.com/user-attachments/assets/a0c0ce14-52e2-4a96-853c-54a231d7a7df" width="200"/>
</div>

Revolutionary location-aware platform that transforms how people discover places and connect with friends through real-time geofencing, AI-driven behavioral psychology, and monetized shop sponsorships.

---

## Vision

PlaceLive emerged from a real college frustration — calling friends one by one asking "Are bhai kaha ho?" when needing something from nearby shops.

This sparked a vision to create the world's most comprehensive place-aware ecosystem, competing directly with Google’s location dominance by owning hyperlocal interaction data.

---

## Unique Value

Unlike existing location apps that are either:

- Privacy-focused (e.g., Find My Friends) or  
- Business-focused (e.g., Google Maps Ads)

PlaceLive uniquely combines:

1. Real-time friend visibility at specific places  
2. AI-powered psychological manipulation for shop recommendations  
3. Monetization model that makes local businesses pay to “go live” in user proximity  

---

## Core Innovations

### Geofenced Shop Sponsorship
- Businesses pay to make their location "live"
- When users enter a 200–500m radius, they receive AI-crafted notifications like:  
  "Best mobile shop in 2km range for quality + price"

### Friend-Request Ecosystem
- Solves the "photocopy problem"
- Users search what they need → app finds friends near relevant shops → auto-sends requests
- No more manual calling!

### Three-Layer Privacy Architecture
- Friend-circle restrictions  
- Place-specific visibility controls  
- Role-based access for private places (e.g., parents, workers)

### AI Behavioral Profiling (Planned)
- Analyzes user emotions (sad, normal, happy)
- Crafts personalized manipulation messages to build trust and drive purchasing decisions

### Generic Microservice Foundation
- Built with a reusable Spring Boot CRUD library
- Services extend `GenericService` to inherit full DB operations → massive development speedup

---

## Technical Architecture

### Overview
- Scalable microservices ecosystem
- 10+ days of architecture planning
- Full working prototype in 30 days

### Microservices

- `placelive-api-gateway` – Routing, auth, rate-limiting  
- `placelive-user-service` – User profiles, auth, friend system  
- `placelive-geofencing` – Location intelligence, shop radius detection  
- `placelive-tracker` – Real-time presence, movement logging, privacy controls  
- `common-library` – Generic CRUD framework via `GenericService`

### Android App
- Built with Kotlin
- Features:
  - Battery-efficient background tracking
  - Real-time notifications
  - Complex privacy UI

---

## Business Model

### Revenue Stream
- Businesses pay subscription fees to sponsor their locations and appear in proximity-based recommendations

### Psychology Edge
- AI will craft emotion-aware messages to drive conversion and trust

### Market Position
- Competing with Google by owning:
  - Who’s where
  - What they think of the place
  - Pricing insights
  - Hyperlocal behavior patterns

---

## Development Journey

- Conception: Sparked from real photocopy-shop frustrations in college
- Planning: 10+ days of full system design before coding
- Execution: Built full app and backend in 30 days
- Validation: Tested by college students → strong pain-point confirmation

---

## Challenges Solved

- Battery Optimization: Efficient real-time tracking without drain  
- Privacy Paradox: Solved with place-based permission models  
- Development Speed: Generic CRUD eliminated redundant code  
- Scalability: Microservices + caching for 1K+ users  
- Trust Building: AI suggestions feel helpful, not ad-like

---

## Future Roadmap

- AI personality modeling for emotion-aware shop suggestions  
- Payment gateway for commissions  
- Expansion to urban commercial zones  
- Analytics dashboard for sponsors  
- Delivery service integration  

---

## Competitive Edge

Unlike:
- Life360 (just family tracking) or
- Google Maps (just locations),

PlaceLive uniquely combines:
- Social connection  
- Real-time commerce  
- Psychological personalization  

The vision extends beyond app success — to own the future of place-intelligence data worldwide.

---
![Visitor Count](https://komarev.com/ghpvc/?username=jeetsolanki&repo=Placelive&style=flat-square&color=blue)

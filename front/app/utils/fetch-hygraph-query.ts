export const fetchHygraphQuery = async (query: string) => {
        const response = await fetch(process.env.HYGRAPH_URL!, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Accept: 'application/json',
                Authorization: `Bearer ${process.env.HYGRAPH_TOKEN}`
            },
            body: JSON.stringify({ query }),
            next: {
                revalidate: 10 * 1 // 10s
            }
        })
        
        const { data } = await response.json()

        return data;
}

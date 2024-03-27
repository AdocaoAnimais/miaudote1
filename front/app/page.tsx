import { fetchHygraphQuery } from './utils/fetch-hygraph-query';
import ListBanco from './components/ListBanco';
import Card from './components/Card.tsx';


const getPageData = async () => {
    const query = `
    query MyQuery {
        posts {
            slug
            title
            rich {
              raw
            }
            picture{
                url
            }
            videoUrl
            tech
            description
        }
        experiences {
            slug
            title
            rich {
              raw
            }
            picture {
                url
            }
            videoUrl
        }
      }
      
    `
    return fetchHygraphQuery(
        query
    )
}

export default async function Page() {
    const response = await getPageData();

    return (
        <>
            <div className='antialiased bg-slate-600 bg-center bg-cover bg-no-repeat min-h-[2000px] items-center flex flex-col'>
                <ListBanco />

                <Card />
            </div>
        </>
    )
}
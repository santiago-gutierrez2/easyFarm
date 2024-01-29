import {useEffect, useState} from "react";
import {getStockChart} from "../../../backend/foodConsumptionService";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";

const StockHomeChart = () => {

    const [stockChartDtos, setStockChartDtos] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const [showChart, setShowChart] = useState(true);
    let optionDefault = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            valueFormatter: (value) => value + ' kg'
        },
        legend: {},
        xAxis: {
            type: 'category',
            data: ['stock']
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} kg'
            }
        },
        series: [
            /*{
                name: 'foodPurchase 1',
                data: [120],
                type: 'bar'
            },
            {
                name: 'foodPurchase 2',
                data: [50],
                type: 'bar'
            }*/
        ]
    };
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        if (isLoading) {
            // get chartData
            getStockChart((stockChartData) => {
                setStockChartDtos(stockChartData);
                console.log(stockChartData);
                if (stockChartData.length > 0) {
                    setChartAttributes(stockChartData);
                }
                setIsLoading(false);
            }, error => {
                setBackendErrors(error);
                setIsLoading(false);
            })
        }
    }, []);

    function setChartAttributes(stockChartData) {
        stockChartData.forEach( stockData => {
            let data = {
                name: stockData.foodPurchaseDto.productName,
                data: [stockData.stockLeft],
                type: 'bar'
            };
            optionDefault.series.push(data);
            setOption(optionDefault);
            console.log(optionDefault);
        });
    }

    if (isLoading) {
        return (
            <div className="row justify-content-center">
                <div className="col-1 text-center">
                    <MoonLoader color="#97C99D" />
                </div>
            </div>
        );
    }

    return (
        <div className="card card-chart">
            <div className="row">
                <div className="col-12 ml-4 mt-2 chart-title" onClick={e => setShowChart(!showChart)}>
                    {showChart &&
                        <h4><b>Food stock <i className="fas fa-caret-up"></i></b></h4>
                    }
                    {!showChart &&
                        <h4><b>Food stock <i className="fas fa-caret-down"></i></b></h4>
                    }
                </div>
            </div>
            <div>
                {showChart &&
                    <ReactEcharts option={option} showLoading={isLoading} lazyUpdate={true}/>
                }
            </div>
        </div>
    );
}

export default StockHomeChart;
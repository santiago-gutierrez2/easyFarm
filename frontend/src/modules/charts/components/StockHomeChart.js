import {useEffect, useState} from "react";
import {getStockChart} from "../../../backend/foodConsumptionService";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";

const StockHomeChart = () => {

    const [stockChartDtos, setStockChartDtos] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);
    const option = {
        xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                data: [120, 200, 150, 80, 70, 110, 130],
                type: 'bar'
            }
        ]
    };

    useEffect(() => {
        if (isLoading) {
            // get chartData
            getStockChart((stockChartData) => {
                setStockChartDtos(stockChartData);
                console.log(stockChartData);
                setIsLoading(false);
                // TODO: set chart atributes
            }, error => {
                setBackendErrors(error);
                setIsLoading(false);
            })
        }
    }, []);

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
      <div>
          <ReactEcharts option={option}/>
      </div>
    );
}

export default StockHomeChart;
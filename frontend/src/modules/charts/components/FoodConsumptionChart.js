import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";
import {getFoodConsumptionChartData} from "../../../backend/foodConsumptionService";


const FoodConsumptionChart = () => {

    const {foodPurchaseId} = useParams();
    const [foodConsumptionChartDtos, setFoodConsumptionChartDtos] = useState([]);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [backendErrors, setBackendErrors] = useState(null);

    let optionDefault = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            valueFormatter: (value) => value + ' kg'
        },
        toolbox: {
            feature: {
                dataView: { show: true, readOnly: true },
                magicType: { show: true, type: ['line', 'bar'] },
                restore: { show: true },
            }
        },
        legend: {},
        dataZoom: [
            {
                show: true,
            },
            {
                type: 'inside',
                show: true

            }
        ],
        xAxis: {
            type: 'category',
            data: [/*'10/12/2023', '11/12/2023', '12/12/2023', '13/12/2023'*/]
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} kg'
            }
        },
        series: [
            /*{
                data: [820, 932, 901, 934, 1290, 1330, 1320],
                type: 'line',
                smooth: true
            }*/
        ]
    };
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        const id = Number(foodPurchaseId);
        if (isLoading) {
            // get chartData
            getFoodConsumptionChartData(id, (chartData) => {
                console.log(chartData);
                let data = {
                    data: [],
                    type: 'line',
                    smooth: true,
                    itemStyle: {
                        color: '#fc5858'
                    },
                    lineStyle: {
                        color: '#00B63E'
                    }
                };
                chartData.forEach(dto => {
                    data.data.push(dto.kilos);
                    optionDefault.xAxis.data.push(dto.consumptionDate);
                });
                optionDefault.series.push(data);
                setOption(optionDefault);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
                setIsLoading(false);
            });
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
      <div className="card card-chart mt-3">
          <div className="row">
              <div className="col-12 ml-4 mt-2 chart-title">
                  <h4><b>Chart of consumption</b></h4>
              </div>
          </div>
          <ReactEcharts option={option} lazyUpdate={true}/>
      </div>
    );

}

export default FoodConsumptionChart;
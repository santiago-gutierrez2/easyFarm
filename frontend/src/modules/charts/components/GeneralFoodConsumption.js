import {useEffect, useState} from "react";
import {getGeneralConsumptionChart} from "../../../backend/foodConsumptionService";
import {MoonLoader} from "react-spinners";
import ReactEcharts from "echarts-for-react";

const GeneralFoodConsumption = () => {

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
        title: {
            text: '',
            left: 'center',
            textStyle: {
                fontSize: 12,
            }
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
            data: []
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value} kg'
            }
        },
        series: []
    };
    const [option, setOption] = useState(optionDefault);

    useEffect(() => {
        if(isLoading) {
            // get chart data
            getGeneralConsumptionChart((chartData) => {
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
                }
                let sumKilos = 0;
                chartData.forEach((dto) => {
                    sumKilos += dto.kilos;
                    data.data.push(dto.kilos);
                    optionDefault.xAxis.data.push(dto.consumptionDate);
                });
                optionDefault.series.push(data);
                optionDefault.title.text = `Average daily consumption: ${(sumKilos/chartData.length).toFixed(2)} kg`;
                setOption(optionDefault);
                setIsLoading(false);
            }, errors => {
                setBackendErrors(errors);
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
      <div className="card card-chart mt-3">
          <div className="row">
              <div className="col-12 ml-4 mt-2 chart-title" onClick={e => setShowChart(!showChart)}>
                  {showChart &&
                      <h4><b>Daily food consumption <i className="fas fa-caret-up"></i></b></h4>
                  }
                  {!showChart &&
                      <h4><b>Daily food consumption <i className="fas fa-caret-down"></i></b></h4>
                  }
              </div>
          </div>
          {showChart &&
              <ReactEcharts option={option} lazyUpdate={true}/>
          }
      </div>
    );
}

export default GeneralFoodConsumption;